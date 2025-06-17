package com.example.shmr_finance_app_android.domain.model

data class Balance(
    val name: String,
    val balance: String,
    val currency: String
) {
    val balanceFormatted: String get() = "$balance $currency"
}