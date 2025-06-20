package com.example.shmr_finance_app_android.data.remote.model

data class AccountResponse(
    val id: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val incomeStats: List<StatItemResponse>,
    val expenseStats: List<StatItemResponse>,
    val createdAt: String,
    val updatedAt: String
)
