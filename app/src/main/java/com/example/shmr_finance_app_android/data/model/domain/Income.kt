package com.example.shmr_finance_app_android.data.model.domain

data class Income(
    val id: String,
    val title: String,
    val amount: Int,
    val currency: String
) {
    val amountFormatted: String get() = "$amount $currency"
}