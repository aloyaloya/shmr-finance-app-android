package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.domain.model.AccountDomain

interface AccountRepository {
    suspend fun getAccountById(accountId: Int): Result<AccountDomain>
}