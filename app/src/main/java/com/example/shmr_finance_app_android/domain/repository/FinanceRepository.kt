package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.model.TransactionDomain

interface FinanceRepository {
    suspend fun getAccountById(accountId: Int): Result<AccountDomain>
    suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDomain>>
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionDomain>>
}