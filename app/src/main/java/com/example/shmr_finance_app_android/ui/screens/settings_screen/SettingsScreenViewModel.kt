package com.example.shmr_finance_app_android.ui.screens.settings_screen

import androidx.lifecycle.ViewModel
import com.example.shmr_finance_app_android.data.model.mockSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor() : ViewModel() {
    private val _darkThemeStatus = MutableStateFlow(false)
    val darkThemeStatus: StateFlow<Boolean> = _darkThemeStatus

    val optionsItems = mockSettings

    fun switchDarkTheme(status: Boolean) {
        _darkThemeStatus.value = status
    }
}