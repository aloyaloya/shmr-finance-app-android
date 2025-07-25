package com.example.shmr_finance_app_android.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.shmr_finance_app_android.BuildConfig
import com.example.shmr_finance_app_android.core.utils.LocaleHelper
import com.example.shmr_finance_app_android.domain.repository.SettingsRepository
import com.example.shmr_finance_app_android.presentation.feature.settings.model.HapticStrength
import com.example.shmr_finance_app_android.presentation.shared.theme.AppColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Named

class SettingsRepositoryImpl @Inject constructor(
    @Named("RegularPrefs") private val sharedPreferences: SharedPreferences,
    private val context: Context
) : SettingsRepository {

    private val _darkThemeFlow = MutableStateFlow(getDarkThemeEnabled())
    private val _appColorFlow = MutableStateFlow(getAppColor())

    override fun getDarkThemeEnabled(): Boolean {
        val enabled = sharedPreferences.getBoolean(KEY_DARK_THEME, false)
        return enabled
    }

    override fun setDarkThemeEnabled(enabled: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_DARK_THEME, enabled).apply()
        _darkThemeFlow.value = enabled
    }

    override fun getDarkThemeFlow(): Flow<Boolean> {
        return _darkThemeFlow.asStateFlow()
    }

    override fun getAppColor(): AppColor {
        val colorName = sharedPreferences.getString(KEY_APP_COLOR, "Blue") ?: "Blue"
        return when (colorName) {
            "Green" -> AppColor.Green
            "Red" -> AppColor.Red
            "Purple" -> AppColor.Purple
            else -> AppColor.Blue
        }
    }

    override fun setAppColor(appColor: AppColor) {
        val colorName = when (appColor) {
            is AppColor.Blue -> "Blue"
            is AppColor.Green -> "Green"
            is AppColor.Red -> "Red"
            is AppColor.Purple -> "Purple"
        }
        sharedPreferences.edit().putString(KEY_APP_COLOR, colorName).apply()
        _appColorFlow.value = appColor
    }

    override fun getAppColorFlow(): Flow<AppColor> {
        return _appColorFlow.asStateFlow()
    }

    override fun setLanguage(language: String) {
        sharedPreferences.edit().putString(KEY_LANGUAGE, language).apply()
        LocaleHelper.applyLocaleToApplication(context, language)
    }

    override fun getLanguage(): String {
        val language = sharedPreferences.getString(KEY_LANGUAGE, "ru") ?: "ru"
        return language
    }

    override fun setHapticStrength(strength: String) {
        sharedPreferences.edit().putString(KEY_HAPTICS_STRENGTH, strength).apply()
    }

    override fun getHapticStrength(): String {
        val strength =
            sharedPreferences.getString(KEY_HAPTICS_STRENGTH, HapticStrength.OFF.name.lowercase())
                ?: HapticStrength.OFF.name.lowercase()

        return strength
    }

    override fun getSyncFrequency(): Int {
        return sharedPreferences.getInt(KEY_SYNC_FREQUENCY, 120)
    }

    override fun setSyncFrequency(minutes: Int) {
        sharedPreferences.edit()
            .putInt(KEY_SYNC_FREQUENCY, minutes)
            .apply()
    }

    override fun getApplicationVersion(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun getLastApplicationUpdate(): Long {
        return BuildConfig.BUILD_TIME.toLong()
    }

    private companion object {
        const val KEY_APP_COLOR = "app_color"
        const val KEY_DARK_THEME = "dark_theme"
        const val KEY_HAPTICS_STRENGTH = "haptic_strength"
        const val KEY_LANGUAGE = "language"
        const val KEY_SYNC_FREQUENCY = "sync_frequency"
    }
}