package com.example.shmr_finance_app_android.presentation.feature.settings.model

import com.example.shmr_finance_app_android.R

sealed class SettingsOption(val titleResId: Int) {
    data object MainColor : SettingsOption(R.string.main_color_option)
    data object Haptics : SettingsOption(R.string.haptics_options)
    data object CodePassword : SettingsOption(R.string.code_password_option)
    data object Synchronize : SettingsOption(R.string.synchronize_option)
    data object Language : SettingsOption(R.string.language_option)
    data object About : SettingsOption(R.string.about_option)

    companion object {
        val items = listOf(
            MainColor,
            Haptics,
            CodePassword,
            Synchronize,
            Language,
            About
        )
    }
}