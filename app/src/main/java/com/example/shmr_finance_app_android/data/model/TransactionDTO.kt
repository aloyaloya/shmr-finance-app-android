package com.example.shmr_finance_app_android.data.model

data class TransactionDTO(
    val id: Int,
    val account: AccountBriefDTO,
    val category: CategoryDTO,
    val amount: String,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)