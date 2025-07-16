package com.example.shmr_finance_app_android.presentation.feature.history.mapper

import com.example.shmr_finance_app_android.core.utils.formatDateAndTime
import com.example.shmr_finance_app_android.core.utils.formatWithSpaces
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import com.example.shmr_finance_app_android.presentation.feature.history.model.TransactionHistoryModel
import javax.inject.Inject

/**
 * Маппер для преобразования [TransactionResponseDomain] -> [TransactionHistoryModel]
 */
class TransactionToTransactionUiMapper @Inject constructor() {
    fun map(domain: TransactionResponseDomain): TransactionHistoryModel {
        return TransactionHistoryModel(
            id = domain.id,
            title = domain.category.name,
            amount = domain.amount,
            currency = domain.account.getCurrencySymbol(),
            subtitle = domain.comment,
            emoji = domain.category.emoji,
            transactionAt = formatDateAndTime(domain.transactionTime, domain.transactionDate)
        )
    }

    fun calculateTotalAmount(transactions: List<TransactionResponseDomain>): String {
        val total = transactions.sumOf { it.amount }
        val currency = transactions.firstOrNull()?.account?.getCurrencySymbol().orEmpty()
        return "${total.toString().formatWithSpaces()} $currency"
    }
}