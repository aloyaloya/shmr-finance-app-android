package com.example.shmr_finance_app_android.ui.screens.settings_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.ui.screens.settings_screen.components.SettingsOptionCard
import com.example.shmr_finance_app_android.ui.screens.settings_screen.components.ThemeSwitcherOptionCard
import com.example.shmr_finance_app_android.ui.viewmodels.SettingsScreenViewModel
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarState
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarViewModel

@Composable
fun SettingsScreen(
    topBarViewModel: TopBarViewModel,
    viewModel: SettingsScreenViewModel = viewModel()  // пока не дошли до DI - вью модель здесь
) {
    val darkThemeStatus by viewModel.darkThemeStatus.collectAsState()
    val optionsItems = viewModel.optionsItems

    val title = stringResource(R.string.settings_screen_title)
    LaunchedEffect(Unit) {
        topBarViewModel.update(
            TopBarState(title = title)
        )
    }

    Column(Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                ThemeSwitcherOptionCard(
                    title = stringResource(R.string.dark_theme_option),
                    isChecked = darkThemeStatus,
                    onCheckedChange = { viewModel.switchDarkTheme(it) }
                )
            }
            items(optionsItems) { option ->
                SettingsOptionCard(
                    title = option.title,
                    onClick = { } // Открытие опции настроек
                )
            }
        }
    }
}