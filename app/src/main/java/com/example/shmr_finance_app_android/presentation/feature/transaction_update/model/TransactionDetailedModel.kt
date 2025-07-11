package com.example.shmr_finance_app_android.presentation.feature.transaction_update.model

data class TransactionDetailedModel(
    val id: Int,
    val accountName: String,
    val categoryId: Int,
    val currency: String,
    val amount: String,
    val comment: String?,
    val date: String,
    val time: String,
    val isIncome: Boolean
)