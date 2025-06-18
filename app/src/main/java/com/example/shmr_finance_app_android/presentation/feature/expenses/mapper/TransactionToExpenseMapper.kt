package com.example.shmr_finance_app_android.presentation.feature.expenses.mapper

import com.example.shmr_finance_app_android.core.utils.formatWithSpaces
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.presentation.feature.expenses.model.ExpenseUiModel
import javax.inject.Inject

class TransactionToExpenseMapper @Inject constructor() {
    fun map(domain: TransactionDomain): ExpenseUiModel {
        return ExpenseUiModel(
            id = domain.id,
            title = domain.category.name,
            amount = domain.amount,
            currency = domain.account.getCurrencySymbol(),
            subtitle = domain.comment,
            emoji = domain.category.emoji
        )
    }

    fun calculateTotalAmount(transactions: List<TransactionDomain>): String {
        val total = transactions.sumOf { it.amount }
        val currency = transactions.firstOrNull()?.account?.getCurrencySymbol().orEmpty()
        return "${total.toString().formatWithSpaces()} $currency"
    }
}