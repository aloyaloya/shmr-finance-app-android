package com.example.shmr_finance_app_android.data.datasources.account

import com.example.shmr_finance_app_android.data.local.entities.AccountEntity

/**
 * Отвечает за получение и изменение данных аккаунта из локальной базы данных.
 * Определяет контракт для работы с данными аккаунта без привязки к конкретной реализации.
 */
interface AccountLocalDataSource {
    suspend fun getAccountById(accountId: Int): AccountEntity?
    suspend fun saveAccount(account: AccountEntity)
    suspend fun updateAccount(account: AccountEntity)
    suspend fun markAccountAsSynced(accountId: Int, updatedAt: String)
    suspend fun getPendingUpdate(): AccountEntity?
}