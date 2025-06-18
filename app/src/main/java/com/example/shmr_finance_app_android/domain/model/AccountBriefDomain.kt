package com.example.shmr_finance_app_android.domain.model

data class AccountBriefDomain(
    val id: Int,
    val name: String,
    val balance: Int,
    val currency: String
) {
    fun getCurrencySymbol(): String {
        return when (currency) {
            "RUB" -> "₽"
            "USD" -> "$"
            "EUR" -> "€"
            else -> currency
        }
    }
}
