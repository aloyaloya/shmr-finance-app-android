package com.example.shmr_finance_app_android.data.repository

import com.example.shmr_finance_app_android.BuildConfig
import com.example.shmr_finance_app_android.core.utils.getCurrentIsoDateTime
import com.example.shmr_finance_app_android.data.datasources.transactions.TransactionsLocalDataSource
import com.example.shmr_finance_app_android.data.datasources.transactions.TransactionsRemoteDataSource
import com.example.shmr_finance_app_android.data.local.entities.TransactionEntity
import com.example.shmr_finance_app_android.data.local.mapper.TransactionEntityMapper
import com.example.shmr_finance_app_android.data.local.model.SyncStatus
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.data.repository.mapper.TransactionsDomainMapper
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * Реализация [TransactionsRepository], отвечающая за:
 * - Получение данных транзакций из удаленного источника ([TransactionsRemoteDataSource])
 * - Преобразование DTO -> доменную модель ([TransactionsDomainMapper])
 * - Обработку ошибок через [safeApiCall]
 */
@Reusable
internal class TransactionsRepositoryImpl @Inject constructor(
    private val localDataSource: TransactionsLocalDataSource,
    private val remoteDataSource: TransactionsRemoteDataSource,
    private val mapper: TransactionsDomainMapper,
    private val entityMapper: TransactionEntityMapper
) : TransactionsRepository {

    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionResponseDomain>> = runCatching {
        val localTransactions = localDataSource.getTransactionsByPeriod(
            accountId = accountId,
            startDate = startDate ?: "",
            endDate = endDate ?: "9999-12-31"
        )
        localTransactions.mapNotNull { entity ->
            val detailed = localDataSource.getTransactionById(entity.id)
            detailed?.let { mapper.mapTransactionResponse(entityMapper.mapTransactionResponse(it)) }
        }
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionDomain> = runCatching {
        val transaction = TransactionEntity(
            id = 0,
            serverId = null,
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment,
            createdAt = getCurrentIsoDateTime(),
            updatedAt = getCurrentIsoDateTime(),
            syncStatus = SyncStatus.PENDING_CREATE
        )

        val newId = localDataSource.insertTransaction(transaction)
        val insertedTransaction = localDataSource.getTransactionById(newId.toInt())
            ?: error("Transaction was not inserted")

        mapper.mapTransaction(entityMapper.mapTransaction(insertedTransaction.transaction))
    }

    override suspend fun getTransactionById(transactionId: Int): Result<TransactionResponseDomain> =
        runCatching {
            val detailed = localDataSource.getTransactionById(transactionId)
                ?: error("Transaction not found")
            mapper.mapTransactionResponse(entityMapper.mapTransactionResponse(detailed))
        }

    override suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionResponseDomain> = runCatching {
        val detailed = localDataSource.getTransactionById(transactionId)
            ?: error("Transaction not found")

        val updated = detailed.transaction.copy(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment,
            syncStatus = SyncStatus.PENDING_UPDATE
        )

        localDataSource.updateTransaction(updated)

        val updatedDetailed = detailed.copy(transaction = updated)
        mapper.mapTransactionResponse(entityMapper.mapTransactionResponse(updatedDetailed))
    }

    override suspend fun deleteTransactionById(transactionId: Int): Result<Unit> = runCatching {
        localDataSource.deleteTransactionById(transactionId)
    }

    override suspend fun syncTransactions(): Result<Unit> = runCatching {
        syncPendingDeletes()
        syncPendingCreatesAndUpdates()
        syncFromRemote()
        localDataSource.purgeDeletedSynced()
    }

    private suspend fun syncPendingDeletes() {
        val pendingDelete = localDataSource.getPendingDeleteTransactions()
        for (transaction in pendingDelete) {
            val serverId = transaction.serverId ?: continue
            remoteDataSource.deleteTransactionById(serverId)
            localDataSource.markTransactionAsSynced(
                localId = transaction.id,
                serverId = serverId,
                updatedAt = getCurrentIsoDateTime()
            )
        }
    }

    private suspend fun syncPendingCreatesAndUpdates() {
        val pendingSync = localDataSource.getPendingSyncTransactions()

        for (transaction in pendingSync) {
            val dto = entityMapper.mapTransaction(transaction)

            try {
                val remote = if (transaction.serverId == null) {
                    val created = remoteDataSource.createTransaction(
                        accountId = dto.accountId,
                        categoryId = dto.categoryId,
                        amount = dto.amount,
                        transactionDate = dto.transactionDate,
                        comment = dto.comment
                    )
                    entityMapper.mapTransactionDtoToResponse(created)
                } else {
                    val remoteCurrent = remoteDataSource.getTransactionById(transaction.serverId)

                    if (remoteCurrent.updatedAt > transaction.updatedAt) {
                        localDataSource.updateTransaction(
                            entityMapper.mapTransactionResponseToEntity(remoteCurrent)
                                .copy(id = transaction.id, syncStatus = SyncStatus.SYNCED)
                        )
                        continue
                    }

                    remoteDataSource.updateTransactionById(
                        transactionId = transaction.serverId,
                        accountId = dto.accountId,
                        categoryId = dto.categoryId,
                        amount = dto.amount,
                        transactionDate = dto.transactionDate,
                        comment = dto.comment
                    )
                }

                localDataSource.markTransactionAsSynced(
                    localId = transaction.id,
                    serverId = remote.id,
                    updatedAt = remote.updatedAt
                )

            } catch (e: retrofit2.HttpException) {
                if (e.code() == 404 && transaction.serverId != null) {
                    localDataSource.markTransactionAsPendingDelete(transaction.id)
                } else {
                    throw e
                }
            }
        }
    }

    private suspend fun syncFromRemote() {
        val remoteTransactions = remoteDataSource.getTransactionsByPeriod(
            accountId = BuildConfig.ACCOUNT_ID,
            startDate = null,
            endDate = null
        )

        val remoteMap = remoteTransactions.associateBy { it.id }
        val localSynced = localDataSource.getSyncedOrPendingUpdateTransactions()

        for (local in localSynced) {
            val remote = remoteMap[local.serverId]
            if (remote == null) {
                localDataSource.deleteTransactionById(local.id)
            }
        }

        for (remote in remoteTransactions) {
            val local = localDataSource.getTransactionByServerId(remote.id)

            if (local == null) {
                val entity = entityMapper.mapTransactionResponseToEntity(remote)
                    .copy(syncStatus = SyncStatus.SYNCED)
                localDataSource.insertTransaction(entity)
            } else if (remote.updatedAt > local.updatedAt && local.syncStatus == SyncStatus.SYNCED) {
                val updated = entityMapper.mapTransactionResponseToEntity(remote)
                    .copy(id = local.id, syncStatus = SyncStatus.SYNCED)

                localDataSource.updateTransaction(updated)
            }
        }
    }
}
