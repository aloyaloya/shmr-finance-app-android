package com.example.shmr_finance_app_android.data.model

data class AccountDTO(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<StatItemDTO>,
    val expenseStats: List<StatItemDTO>,
    val createdAt: String,
    val updatedAt: String
)