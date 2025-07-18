package com.example.shmr_finance_app_android.data.datasources.transactions

import com.example.shmr_finance_app_android.core.utils.getCurrentIsoDateTime
import com.example.shmr_finance_app_android.data.local.dao.TransactionDao
import com.example.shmr_finance_app_android.data.local.entities.TransactionEntity
import com.example.shmr_finance_app_android.data.local.mapper.TransactionEntityMapper
import com.example.shmr_finance_app_android.data.local.model.SyncStatus
import com.example.shmr_finance_app_android.data.local.model.TransactionDetailed
import javax.inject.Inject

/**
 * Реализация [TransactionsLocalDataSource], отвечающая за:
 * - Запрос данных транзакций из локальной базы данных
 * - Запрос создания создания в локальной базе данных
 * - Запрос удаления транзакций из локальной базы данных
 * - Запрос изменения данных транзакций в локальной базе данных
 * - Преобразование Entity моделей в DTO
 *
 * @param dao - dao для работы с локальной базой данных [TransactionDao]
 * @param mapper - маппер для преобразования Entity моделей [TransactionEntityMapper]
 */
class TransactionsLocalDataSourceImpl @Inject constructor(
    private val dao: TransactionDao
) : TransactionsLocalDataSource {

    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionEntity> {
        return dao.getByPeriod(
            accountId = accountId,
            start = toStartOfDayIso(startDate),
            end = toEndOfDayIso(endDate)
        )
            .map { it.transaction }
    }

    override suspend fun getTransactionById(id: Int): TransactionDetailed? {
        return dao.getById(id)
    }

    override suspend fun insertTransaction(transaction: TransactionEntity): Long {
        return dao.insert(transaction)
    }

    override suspend fun insertTransactions(transactions: List<TransactionEntity>) {
        dao.insertAll(transactions)
    }

    override suspend fun updateTransaction(transaction: TransactionEntity) {
        val updatedAt = getCurrentIsoDateTime()

        val updatedTransaction = transaction.copy(
            syncStatus = SyncStatus.PENDING_UPDATE,
            updatedAt = updatedAt
        )

        dao.update(updatedTransaction)
    }

    override suspend fun deleteTransactionById(id: Int) {
        val existing = dao.getById(id)?.transaction ?: return
        dao.update(existing.copy(syncStatus = SyncStatus.PENDING_DELETE))
    }

    override suspend fun getPendingSyncTransactions(): List<TransactionEntity> {
        return dao.getPendingSync()
    }

    override suspend fun getPendingDeleteTransactions(): List<TransactionEntity> {
        return dao.getPendingDeletions()
    }

    override suspend fun getSyncedOrPendingUpdateTransactions(): List<TransactionEntity> {
        return dao.getSyncedOrPendingUpdateTransactions()
    }

    override suspend fun markTransactionAsSynced(
        localId: Int,
        serverId: Int,
        updatedAt: String
    ) {
        dao.markAsSynced(localId, serverId, updatedAt)
    }

    override suspend fun purgeDeletedSynced() {
        dao.purgeDeletedSynced()
    }

    override suspend fun getTransactionByServerId(serverId: Int): TransactionEntity? {
        return dao.getByServerId(serverId)
    }

    override suspend fun getSyncedTransactions(): List<TransactionEntity> {
        return dao.getAllWithStatus(SyncStatus.SYNCED.name)
    }

    override suspend fun markTransactionAsPendingDelete(localId: Int) {
        dao.updateSyncStatus(localId, SyncStatus.PENDING_DELETE.name)
    }
}

private fun toStartOfDayIso(date: String): String = "${date}T00:00:00"
private fun toEndOfDayIso(date: String): String = "${date}T23:59:59.999"
