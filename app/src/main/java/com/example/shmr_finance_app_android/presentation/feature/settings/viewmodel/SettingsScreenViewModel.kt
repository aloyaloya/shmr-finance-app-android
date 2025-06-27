package com.example.shmr_finance_app_android.presentation.feature.settings.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel для экрана Настройки, реализующая:
 * 1. Смену темы (темная, светлая) [switchDarkTheme]
 **/
@HiltViewModel
class SettingsScreenViewModel @Inject constructor() : ViewModel() {

    private val _darkThemeStatus = MutableStateFlow(false)
    val darkThemeStatus: StateFlow<Boolean> = _darkThemeStatus

    fun switchDarkTheme(status: Boolean) {
        _darkThemeStatus.value = status
    }
}