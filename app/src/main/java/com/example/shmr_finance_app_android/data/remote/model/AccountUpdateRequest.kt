package com.example.shmr_finance_app_android.data.remote.model

data class AccountUpdateRequest(
    val name: String,
    val balance: String,
    val currency: String
)