package com.example.shmr_finance_app_android.presentation.feature.settings.screen

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.MainActivity
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.di.daggerViewModel
import com.example.shmr_finance_app_android.core.utils.formatTimestamp
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.feature.settings.component.AboutSheet
import com.example.shmr_finance_app_android.presentation.feature.settings.component.PropertySelectionSheet
import com.example.shmr_finance_app_android.presentation.feature.settings.component.SyncSliderSheet
import com.example.shmr_finance_app_android.presentation.feature.settings.component.ThemeSwitcherOptionCard
import com.example.shmr_finance_app_android.presentation.feature.settings.model.SettingsOption
import com.example.shmr_finance_app_android.presentation.feature.settings.viewmodel.SettingsScreenViewModel
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = daggerViewModel(),
    updateConfigState: (ScreenConfig) -> Unit,
    onPinUpdateNavigate: () -> Unit
) {
    val darkThemeStatus by viewModel.darkThemeStatus.collectAsStateWithLifecycle()
    val activeBottomSheet by viewModel.activeBottomSheet.collectAsStateWithLifecycle()
    val lastSync by viewModel.lastSyncTime.collectAsStateWithLifecycle()
    val syncFrequency by viewModel.syncFrequency.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.languageChanged.collect {
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            (context as? Activity)?.finish()
        }
    }

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(titleResId = R.string.settings_screen_title)
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
            items(SettingsOption.items) { option ->
                val optionTitle = stringResource(option.titleResId)
                ListItemCard(
                    modifier = Modifier
                        .clickable {
                            if (option is SettingsOption.CodePassword) {
                                onPinUpdateNavigate()
                            } else {
                                viewModel.showBottomSheet(option)
                            }
                        }
                        .height(56.dp),
                    item = ListItem(content = MainContent(title = optionTitle)),
                    trailIcon = R.drawable.ic_filled_arrow_right
                )
            }
        }
        Spacer(Modifier.height(dimensionResource(R.dimen.medium_spacer)))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.medium_padding)),
            text = stringResource(R.string.last_sync),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.medium_padding)),
            text = formatTimestamp(lastSync),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }

    activeBottomSheet?.let { option ->
        when (option) {
            SettingsOption.Synchronize -> {
                SyncSliderSheet(
                    currentMinutes = syncFrequency,
                    onMinutesSelected = viewModel::setSyncFrequency,
                    onDismiss = { viewModel.hideBottomSheet() }
                )
            }

            SettingsOption.About -> {
                AboutSheet(
                    version = viewModel.getVersionName(),
                    lastUpdate = viewModel.getLastUpdate(),
                    onDismiss = { viewModel.hideBottomSheet() }
                )
            }

            else -> {
                PropertySelectionSheet(
                    items = viewModel.getItemsForOption(option, darkThemeStatus),
                    onItemSelected = { viewModel.onItemSelected(option, it) },
                    onDismiss = { viewModel.hideBottomSheet() }
                )
            }
        }
    }
}