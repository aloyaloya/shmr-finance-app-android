package com.example.shmr_finance_app_android.presentation.feature.balance.models

import com.example.shmr_finance_app_android.core.utils.formatWithSpaces

data class BalanceUiModel(
    val id: Int,
    val amount: Int,
    val currency: String
) {
    val balanceFormatted: String
        get() = "${amount.toString().formatWithSpaces()} $currency"
}