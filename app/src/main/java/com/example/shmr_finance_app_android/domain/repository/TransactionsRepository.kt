package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain

/**
 * Репозиторий для работы с данными транзакций.
 **/
interface TransactionsRepository {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionResponseDomain>>

    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionDomain>

    suspend fun getTransactionById(transactionId: Int): Result<TransactionResponseDomain>

    suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionResponseDomain>

    suspend fun deleteTransactionById(transactionId: Int): Result<Unit>

    suspend fun syncTransactions(): Result<Unit>
}