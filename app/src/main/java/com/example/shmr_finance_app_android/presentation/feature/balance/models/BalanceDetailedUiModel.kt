package com.example.shmr_finance_app_android.presentation.feature.balance.models

data class BalanceDetailedUiModel(
    val id: Int,
    val name: String,
    val amount: String,
    val currencyCode: String,
    val currencySymbol: String
)