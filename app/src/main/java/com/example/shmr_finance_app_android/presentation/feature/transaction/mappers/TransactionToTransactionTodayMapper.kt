package com.example.shmr_finance_app_android.presentation.feature.transaction.mappers

import com.example.shmr_finance_app_android.core.utils.formatWithSpaces
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import com.example.shmr_finance_app_android.presentation.feature.transaction.models.TransactionTodayModel
import javax.inject.Inject


/**
 * Маппер для преобразования [TransactionResponseDomain] -> [TransactionTodayModel]
 */
class TransactionToTransactionTodayMapper @Inject constructor() {
    fun map(domain: TransactionResponseDomain): TransactionTodayModel {
        return TransactionTodayModel(
            id = domain.id,
            title = domain.category.name,
            amount = domain.amount.toString(),
            currency = domain.account.getCurrencySymbol(),
            subtitle = domain.comment,
            emoji = domain.category.emoji
        )
    }

    fun calculateTotalAmount(transactions: List<TransactionResponseDomain>): String {
        val total = transactions.sumOf { it.amount }
        val currency = transactions.firstOrNull()?.account?.getCurrencySymbol().orEmpty()
        return "${total.toString().formatWithSpaces()} $currency"
    }
}