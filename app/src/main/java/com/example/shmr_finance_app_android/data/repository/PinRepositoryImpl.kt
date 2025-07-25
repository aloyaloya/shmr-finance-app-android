package com.example.shmr_finance_app_android.data.repository

import android.content.SharedPreferences
import com.example.shmr_finance_app_android.domain.repository.PinRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class PinRepositoryImpl @Inject constructor(
    @Named("EncryptedPrefs") private val encryptedSharedPreferences: SharedPreferences
) : PinRepository {

    override fun savePin(pin: String) {
        encryptedSharedPreferences.edit().putString(KEY_PIN, pin).apply()
    }

    override fun checkPin(pin: String): Boolean {
        val savedPin = encryptedSharedPreferences.getString(KEY_PIN, null)
        return savedPin == pin
    }

    override fun hasPin(): Boolean {
        return encryptedSharedPreferences.contains(KEY_PIN)
    }

    override fun clearPin() {
        encryptedSharedPreferences.edit().remove(KEY_PIN).apply()
    }

    private companion object {
        const val KEY_PIN = "pin_code"
    }
}