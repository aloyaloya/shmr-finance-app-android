package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.presentation.shared.theme.AppColor
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getDarkThemeEnabled(): Boolean
    fun setDarkThemeEnabled(enabled: Boolean)
    fun getDarkThemeFlow(): Flow<Boolean>
    fun getAppColor(): AppColor
    fun setAppColor(appColor: AppColor)
    fun getAppColorFlow(): Flow<AppColor>
    fun setLanguage(language: String)
    fun getLanguage(): String
    fun setHapticStrength(strength: String)
    fun getHapticStrength(): String
    fun getSyncFrequency(): Int
    fun setSyncFrequency(minutes: Int)
    fun getApplicationVersion(): String
    fun getLastApplicationUpdate(): Long
}