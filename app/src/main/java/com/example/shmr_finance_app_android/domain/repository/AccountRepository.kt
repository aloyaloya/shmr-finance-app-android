package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.domain.model.AccountDomain

/**
 * Репозиторий для работы с данными аккаунтов.
 **/
interface AccountRepository {
    suspend fun getAccountById(accountId: Int): Result<AccountDomain>
}