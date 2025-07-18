package com.example.shmr_finance_app_android.presentation.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shmr_finance_app_android.data.sync.SyncPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel для экрана Настройки, реализующая:
 * 1. Смену темы (темная, светлая) [switchDarkTheme]
 **/
class SettingsScreenViewModel @Inject constructor(
    private val syncPrefs: SyncPrefs
) : ViewModel() {

    private val _darkThemeStatus = MutableStateFlow(false)
    val darkThemeStatus: StateFlow<Boolean> = _darkThemeStatus

    private val _lastSyncTime = MutableStateFlow(0L)
    val lastSyncTime: StateFlow<Long> = _lastSyncTime

    init {
        _lastSyncTime.value = syncPrefs.getLastSyncTime()
    }

    fun switchDarkTheme(status: Boolean) {
        _darkThemeStatus.value = status
    }

    fun updateLastSyncTime() {
        val currentTime = System.currentTimeMillis()
        syncPrefs.saveLastSyncTime(currentTime)
        _lastSyncTime.value = currentTime
    }
}