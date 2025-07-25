package com.example.shmr_finance_app_android.presentation.shared.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ThemeViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _isDarkTheme = MutableStateFlow(settingsRepository.getDarkThemeEnabled())
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _appColor = MutableStateFlow(settingsRepository.getAppColor())
    val appColor: StateFlow<AppColor> = _appColor.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.getDarkThemeFlow().collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
        viewModelScope.launch {
            settingsRepository.getAppColorFlow().collect { color ->
                _appColor.value = color
            }
        }
    }
}