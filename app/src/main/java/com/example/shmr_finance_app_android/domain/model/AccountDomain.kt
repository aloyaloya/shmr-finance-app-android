package com.example.shmr_finance_app_android.domain.model

data class AccountDomain(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: String,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)