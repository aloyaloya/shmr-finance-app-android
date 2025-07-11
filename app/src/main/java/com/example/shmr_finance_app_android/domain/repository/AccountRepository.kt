package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.domain.model.AccountBriefDomain
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.model.AccountResponseDomain

/**
 * Репозиторий для работы с данными аккаунтов.
 **/
interface AccountRepository {
    suspend fun getAccountById(accountId: Int): Result<AccountResponseDomain>
    suspend fun updateAccountById(accountBrief: AccountBriefDomain): Result<AccountDomain>
}