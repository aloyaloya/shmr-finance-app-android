package com.example.shmr_finance_app_android.presentation.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.core.di.HapticFeedbackManager
import com.example.shmr_finance_app_android.data.sync.SyncPrefs
import com.example.shmr_finance_app_android.domain.repository.SettingsRepository
import com.example.shmr_finance_app_android.presentation.feature.settings.model.PropertyModel
import com.example.shmr_finance_app_android.presentation.feature.settings.model.PropertyModels
import com.example.shmr_finance_app_android.presentation.feature.settings.model.SettingsOption
import com.example.shmr_finance_app_android.presentation.shared.theme.AppColor
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel для экрана Настройки, реализующая:
 * 1. Смену темы (темная, светлая) [switchDarkTheme]
 **/
class SettingsScreenViewModel @Inject constructor(
    syncPrefs: SyncPrefs,
    private val settingsRepository: SettingsRepository,
    private val hapticFeedbackManager: HapticFeedbackManager
) : ViewModel() {

    private val _lastSyncTime = MutableStateFlow(syncPrefs.getLastSyncTime())
    val lastSyncTime: StateFlow<Long> = _lastSyncTime.asStateFlow()

    private val _darkThemeStatus = MutableStateFlow(settingsRepository.getDarkThemeEnabled())
    val darkThemeStatus: StateFlow<Boolean> = _darkThemeStatus.asStateFlow()

    private val _activeBottomSheet = MutableStateFlow<SettingsOption?>(null)
    val activeBottomSheet: StateFlow<SettingsOption?> = _activeBottomSheet.asStateFlow()

    private val _languageChanged = MutableSharedFlow<Unit>()
    val languageChanged: SharedFlow<Unit> = _languageChanged.asSharedFlow()

    private val _syncFrequency = MutableStateFlow(settingsRepository.getSyncFrequency())
    val syncFrequency: StateFlow<Int> = _syncFrequency.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getDarkThemeFlow().collect { isDark ->
                if (_darkThemeStatus.value != isDark) {
                    _darkThemeStatus.value = isDark
                }
            }
        }
    }

    fun switchDarkTheme(status: Boolean) {
        settingsRepository.setDarkThemeEnabled(status)
        hapticFeedbackManager.vibrateClick()
    }

    fun setAppColor(appColor: AppColor) {
        settingsRepository.setAppColor(appColor)
        hapticFeedbackManager.vibrateClick()
    }

    fun showBottomSheet(option: SettingsOption) {
        _activeBottomSheet.value = option
    }

    fun hideBottomSheet() {
        _activeBottomSheet.value = null
    }

    fun getItemsForOption(option: SettingsOption, isDarkTheme: Boolean): List<PropertyModel> {
        return when (option) {
            is SettingsOption.MainColor -> PropertyModels.getColors(isDarkTheme)
            is SettingsOption.Language -> PropertyModels.getLanguages()
            is SettingsOption.Haptics -> PropertyModels.getHaptics()
            else -> emptyList()
        }
    }

    fun setSyncFrequency(minutes: Int) {
        settingsRepository.setSyncFrequency(minutes)
        _syncFrequency.value = minutes
        hapticFeedbackManager.vibrateClick()
    }

    fun getVersionName(): String {
        return settingsRepository.getApplicationVersion()
    }

    fun getLastUpdate(): Long {
        return settingsRepository.getLastApplicationUpdate()
    }

    fun onItemSelected(option: SettingsOption, item: PropertyModel) {
        when (option) {
            is SettingsOption.MainColor -> {
                val appColor = when (item.id) {
                    "Blue" -> AppColor.Blue
                    "Green" -> AppColor.Green
                    "Red" -> AppColor.Red
                    "Purple" -> AppColor.Purple
                    else -> return
                }
                setAppColor(appColor)
            }

            is SettingsOption.Language -> {
                settingsRepository.setLanguage(item.id)
                hapticFeedbackManager.vibrateClick()

                viewModelScope.launch {
                    _languageChanged.emit(Unit)
                }
            }

            is SettingsOption.Haptics -> {
                settingsRepository.setHapticStrength(item.id)
                hapticFeedbackManager.vibrateFromSettings()
            }

            else -> {}
        }
    }
}