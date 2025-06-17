package com.example.shmr_finance_app_android.navigation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.shmr_finance_app_android.navigation.config.TopBarConfig
import com.example.shmr_finance_app_android.navigation.routes.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    config: TopBarConfig,
    onBack: () -> Unit,
    onActionRoute: (String) -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(config.titleResId))
        },
        navigationIcon = {
            if (config.showBackButton) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }
        },
        actions = {
            config.action?.let { action ->
                IconButton(
                    onClick = { onActionRoute(action.actionRoute.path) }
                ) {
                    Icon(
                        painter = painterResource(action.iconResId),
                        contentDescription = stringResource(action.descriptionResId)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    )
}