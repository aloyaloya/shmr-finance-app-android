package com.example.shmr_finance_app_android.data.remote.mapper

import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.model.TransactionResponseDTO
import com.example.shmr_finance_app_android.data.remote.model.AccountBrief
import com.example.shmr_finance_app_android.data.remote.model.Transaction
import com.example.shmr_finance_app_android.data.remote.model.TransactionRequest
import com.example.shmr_finance_app_android.data.remote.model.TransactionResponse
import dagger.Reusable
import javax.inject.Inject

/**
 * Маппер для:
 * Преобразования [TransactionResponse] -> [TransactionResponseDTO] при получении данных
 * Преобразования [TransactionResponseDTO] -> [TransactionRequest] при изменении данных
 * Делегирует маппинг категории к [CategoryRemoteMapper]
 * Создает краткую версию данных аккаунта [AccountBriefDTO]
 */
@Reusable
internal class TransactionsRemoteMapper @Inject constructor(
    private val categoryMapper: CategoryRemoteMapper
) {
    fun mapTransactionResponse(response: TransactionResponse): TransactionResponseDTO {
        return TransactionResponseDTO(
            id = response.id,
            account = mapAccountBrief(response.account),
            category = categoryMapper.mapCategory(response.category),
            amount = response.amount,
            transactionDate = response.transactionDate,
            comment = response.comment,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt
        )
    }

    private fun mapAccountBrief(brief: AccountBrief): AccountBriefDTO {
        return AccountBriefDTO(
            id = brief.id,
            name = brief.name,
            balance = brief.balance,
            currency = brief.currency
        )
    }

    fun mapTransaction(transaction: Transaction): TransactionDTO {
        return TransactionDTO(
            id = transaction.id,
            accountId = transaction.accountId,
            categoryId = transaction.categoryId,
            amount = transaction.amount,
            transactionDate = transaction.transactionDate,
            comment = transaction.comment,
            createdAt = transaction.createdAt,
            updatedAt = transaction.updatedAt
        )
    }

    fun mapTransactionToRequest(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionRequest {
        return TransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = transactionDate,
            comment = comment
        )
    }
}