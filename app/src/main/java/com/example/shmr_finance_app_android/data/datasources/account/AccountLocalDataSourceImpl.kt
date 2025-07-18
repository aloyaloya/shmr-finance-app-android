package com.example.shmr_finance_app_android.data.datasources.account

import com.example.shmr_finance_app_android.core.utils.getCurrentIsoDateTime
import com.example.shmr_finance_app_android.data.local.dao.AccountDao
import com.example.shmr_finance_app_android.data.local.entities.AccountEntity
import com.example.shmr_finance_app_android.data.local.model.SyncStatus
import javax.inject.Inject

class AccountLocalDataSourceImpl @Inject constructor(
    private val dao: AccountDao
) : AccountLocalDataSource {

    override suspend fun getAccountById(accountId: Int): AccountEntity? {
        return dao.getById(accountId)
    }

    override suspend fun saveAccount(account: AccountEntity) {
        dao.insert(account)
    }

    override suspend fun updateAccount(account: AccountEntity) {
        val updatedAt = getCurrentIsoDateTime()

        val updatedAccount = account.copy(
            syncStatus = SyncStatus.PENDING_UPDATE,
            updatedAt = updatedAt
        )

        dao.update(updatedAccount)
    }

    override suspend fun markAccountAsSynced(accountId: Int, updatedAt: String) {
        dao.markAsSynced(id = accountId, updatedAt = updatedAt)
    }

    override suspend fun getPendingUpdate(): AccountEntity? {
        return dao.getPendingUpdate()
    }
}