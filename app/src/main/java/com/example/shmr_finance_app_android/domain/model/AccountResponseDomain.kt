package com.example.shmr_finance_app_android.domain.model

import java.time.LocalDate
import java.time.LocalTime

data class AccountResponseDomain(
    val id: Int,
    val name: String,
    val balance: Int,
    val currency: String,
    val incomeStats: List<StatItemDomain>,
    val expenseStats: List<StatItemDomain>,
    val createdAtDate: LocalDate,
    val createdAtTime: LocalTime,
    val updatedAtDate: LocalDate,
    val updatedAtTime: LocalTime
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
