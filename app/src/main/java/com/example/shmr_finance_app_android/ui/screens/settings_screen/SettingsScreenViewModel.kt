package com.example.shmr_finance_app_android.ui.screens.settings_screen

import androidx.lifecycle.ViewModel
import com.example.shmr_finance_app_android.data.model.mockSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SettingsScreenViewModel : ViewModel() {
    private val _darkThemeStatus = MutableStateFlow(false)
    val darkThemeStatus: StateFlow<Boolean> = _darkThemeStatus

    val optionsItems = mockSettings

    fun switchDarkTheme(status: Boolean) {
        _darkThemeStatus.value = status
    }
}