package com.example.shmr_finance_app_android.core.navigation

import androidx.annotation.StringRes
import com.example.shmr_finance_app_android.R

/**
 * Отвечает за представление элемента списка настроек:
 * @property titleResId - заголовок опции настройки из ресурсов
 */
data class SettingsListItem(
    @StringRes val titleResId: Int
) {
    /**
     * Отвечает за хранение и предоставление всех элементов списка настроек.
     */
    companion object {
        val items = listOf(
            SettingsListItem(
                titleResId = R.string.main_color_option,
            ),
            SettingsListItem(
                titleResId = R.string.sounds_options,
            ),
            SettingsListItem(
                titleResId = R.string.haptics_options,
            ),
            SettingsListItem(
                titleResId = R.string.code_password_option,
            ),
            SettingsListItem(
                titleResId = R.string.synchronize_option,
            ),
            SettingsListItem(
                titleResId = R.string.language_option,
            ),
            SettingsListItem(
                titleResId = R.string.about_option,
            )
        )
    }
}