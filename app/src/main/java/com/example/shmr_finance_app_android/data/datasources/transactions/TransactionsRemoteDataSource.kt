package com.example.shmr_finance_app_android.data.datasources.transactions

import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.model.TransactionResponseDTO
import retrofit2.Response

/**
 * Отвечает за работу с данными из remote-источника (API).
 * Определяет контракт для работы с данными транзакций без привязки к конкретной реализации.
 */
interface TransactionsRemoteDataSource {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionResponseDTO>

    suspend fun getTransactionById(transactionId: Int): TransactionResponseDTO

    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionDTO

    suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionResponseDTO

    suspend fun deleteTransactionById(transactionId: Int): Response<Void>
}