package com.example.shmr_finance_app_android.domain.repository

interface PinRepository {
    fun savePin(pin: String)
    fun checkPin(pin: String): Boolean
    fun hasPin(): Boolean
    fun clearPin()
}