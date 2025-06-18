package com.example.shmr_finance_app_android.data.dto

data class AccountDTO(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: StatItemDTO,
    val expenseStats: StatItemDTO,
    val createdAt: String,
    val updatedAt: String
)