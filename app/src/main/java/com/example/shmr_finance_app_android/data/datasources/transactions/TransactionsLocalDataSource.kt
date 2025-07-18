package com.example.shmr_finance_app_android.data.datasources.transactions

import com.example.shmr_finance_app_android.data.local.entities.TransactionEntity
import com.example.shmr_finance_app_android.data.local.model.TransactionDetailed

/**
 * Отвечает за работу с данными из локальной базы данных.
 * Определяет контракт для работы с данными транзакций без привязки к конкретной реализации.
 */
interface TransactionsLocalDataSource {

    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionEntity>

    suspend fun getTransactionById(id: Int): TransactionDetailed?
    suspend fun insertTransaction(transaction: TransactionEntity): Long
    suspend fun insertTransactions(transactions: List<TransactionEntity>)
    suspend fun updateTransaction(transaction: TransactionEntity)
    suspend fun deleteTransactionById(id: Int)
    suspend fun getPendingSyncTransactions(): List<TransactionEntity>
    suspend fun getPendingDeleteTransactions(): List<TransactionEntity>
    suspend fun getSyncedOrPendingUpdateTransactions(): List<TransactionEntity>

    suspend fun markTransactionAsSynced(
        localId: Int,
        serverId: Int,
        updatedAt: String
    )

    suspend fun purgeDeletedSynced()
    suspend fun getTransactionByServerId(serverId: Int): TransactionEntity?
    suspend fun getSyncedTransactions(): List<TransactionEntity>
    suspend fun markTransactionAsPendingDelete(localId: Int)
}