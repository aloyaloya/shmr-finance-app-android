package com.example.shmr_finance_app_android.data.datasources.account

import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.AccountDTO
import com.example.shmr_finance_app_android.data.model.AccountResponseDTO

/**
 * Отвечает за получение и изменение данных аккаунта из источника (API).
 * Определяет контракт для работы с данными аккаунта без привязки к конкретной реализации.
 */
interface AccountRemoteDataSource {
    suspend fun getAccountById(accountId: Int): AccountResponseDTO
    suspend fun updateAccountById(accountBrief: AccountBriefDTO): AccountDTO
}