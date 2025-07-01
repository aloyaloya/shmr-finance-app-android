package com.example.shmr_finance_app_android.presentation.feature.balance_edit.model

data class BalanceDetailedUiModel(
    val id: Int,
    val name: String,
    val amount: Int,
    val currencyCode: String,
    val currencySymbol: String
)