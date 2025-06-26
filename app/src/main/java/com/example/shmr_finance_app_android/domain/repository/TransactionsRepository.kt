package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.domain.model.TransactionDomain

interface TransactionsRepository {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionDomain>>
}