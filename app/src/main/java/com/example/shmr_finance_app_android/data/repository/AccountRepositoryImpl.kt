package com.example.shmr_finance_app_android.data.repository

import com.example.shmr_finance_app_android.data.datasources.account.AccountLocalDataSource
import com.example.shmr_finance_app_android.data.datasources.account.AccountRemoteDataSource
import com.example.shmr_finance_app_android.data.local.mapper.AccountEntityMapper
import com.example.shmr_finance_app_android.data.local.model.SyncStatus
import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.data.repository.mapper.AccountDomainMapper
import com.example.shmr_finance_app_android.domain.model.AccountBriefDomain
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.model.AccountResponseDomain
import com.example.shmr_finance_app_android.domain.repository.AccountRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * Реализация [AccountRepository], отвечающая за:
 * - Получение данных аккаунта из удаленного источника ([AccountRemoteDataSource])
 * - Получение данных аккаунта из локального источника ([AccountLocalDataSource])
 * - Изменение валюты счета
 * - Преобразование DTO -> доменную модель ([AccountDomainMapper])
 * - Обработку ошибок через [safeApiCall]
 */
@Reusable
internal class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountRemoteDataSource,
    private val localDataSource: AccountLocalDataSource,
    private val mapper: AccountDomainMapper,
    private val entityMapper: AccountEntityMapper
) : AccountRepository {

    override suspend fun getAccountById(accountId: Int): Result<AccountResponseDomain> =
        runCatching {
            val local = localDataSource.getAccountById(accountId)
            if (local != null) {
                val dto = entityMapper.mapAccountResponse(local)
                mapper.mapAccountResponse(dto)
            } else {
                val remote = remoteDataSource.getAccountById(accountId)
                val entity = entityMapper.mapAccountResponseToEntity(remote)
                localDataSource.saveAccount(entity)
                mapper.mapAccountResponse(remote)
            }
        }

    override suspend fun updateAccountById(accountBrief: AccountBriefDomain): Result<AccountDomain> =
        runCatching {
            val entity = localDataSource.getAccountById(accountBrief.id)?.copy(
                name = accountBrief.name,
                balance = accountBrief.balance.toString(),
                currency = accountBrief.currency
            ) ?: error("Account is not found")

            localDataSource.updateAccount(entity)
            mapper.mapAccount(entityMapper.mapAccount(entity))
        }

    override suspend fun syncAccounts(): Result<Unit> = runCatching {
        val pending = localDataSource.getPendingUpdate()
        if (pending != null) {
            val remote = remoteDataSource.getAccountById(pending.id)

            if (remote.updatedAt > pending.updatedAt) {
                localDataSource.updateAccount(
                    entityMapper.mapAccountResponseToEntity(remote)
                        .copy(syncStatus = SyncStatus.SYNCED)
                )
            } else {
                val dto = AccountBriefDTO(
                    id = pending.id,
                    name = pending.name,
                    balance = pending.balance,
                    currency = pending.currency
                )

                val updated = remoteDataSource.updateAccountById(dto)

                localDataSource.markAccountAsSynced(
                    accountId = dto.id,
                    updatedAt = updated.updatedAt
                )
            }
        }
    }
}
