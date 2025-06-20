package com.example.shmr_finance_app_android.presentation.feature.settings.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.core.navigation.SettingsListItems
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.feature.settings.viewmodel.SettingsScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.settings.component.ThemeSwitcherOptionCard

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val darkThemeStatus by viewModel.darkThemeStatus.collectAsState()

    LaunchedEffect(updateConfigState) {
        updateConfigState(
            ScreenConfig(
                route = Route.Root.Settings.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.settings_screen_title
                )
            )
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
            items(SettingsListItems.items) { option ->
                val optionTitle = stringResource(option.titleResId)
                ListItemCard(
                    modifier = Modifier
                        .clickable { } // Переход пока не понятно куда
                        .height(56.dp),
                    item = ListItem(content = MainContent(title = optionTitle)),
                    trailIcon = R.drawable.ic_filled_arrow_right
                )
            }
        }
    }
}