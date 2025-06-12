package com.example.shmr_finance_app_android.ui.navigation

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
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    state: TopBarState,
    onBack: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(state.title) },
        navigationIcon = {
            if (state.showBackButton) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Назад"
                    )
                }
            }
        },
        actions = {
            state.actionIcon?.let { icon ->
                IconButton(onClick = { state.onActionClick?.invoke() }) {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = state.actionDescription
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        )
    )
}