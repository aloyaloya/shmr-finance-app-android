package com.example.shmr_finance_app_android.presentation.feature.transaction_update.mapper

import com.example.shmr_finance_app_android.core.utils.toHumanDate
import com.example.shmr_finance_app_android.core.utils.toHumanTime
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import com.example.shmr_finance_app_android.presentation.feature.transaction_update.model.TransactionDetailedModel
import javax.inject.Inject

class TransactionResponseToTransactionDetailed @Inject constructor() {
    fun map(domain: TransactionResponseDomain): TransactionDetailedModel {
        return TransactionDetailedModel(
            id = domain.id,
            accountName = domain.account.name,
            categoryId = domain.category.id,
            amount = domain.amount.toString(),
            currency = domain.account.getCurrencySymbol(),
            comment = domain.comment,
            date = domain.transactionDate.toHumanDate(),
            time = domain.transactionTime.toHumanTime(),
            isIncome = domain.category.isIncome
        )
    }
}