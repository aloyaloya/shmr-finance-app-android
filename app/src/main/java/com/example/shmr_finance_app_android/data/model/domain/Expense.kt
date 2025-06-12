package com.example.shmr_finance_app_android.data.model.domain

data class Expense(
    val id: String,
    val title: String,
    val amount: Int,
    val currency: String,
    val author: String? = null,
    val emoji: String? = null
) {
    val amountFormatted: String get() = "$amount $currency"
}