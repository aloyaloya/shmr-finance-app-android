package com.example.shmr_finance_app_android.presentation.feature.main.component

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
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarBackAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    config: TopBarConfig,
    onClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = stringResource(config.titleResId))
        },
        navigationIcon = {
            config.backAction?.let { action ->
                BackAction(
                    backActionConfig = config.backAction,
                    onBack = {
                        onClick()
                        action.actionUnit.invoke()
                    }
                )
            }
        },
        actions = {
            config.action?.let { action ->
                IconButton(
                    onClick = {
                        if (action.isActive.invoke()) {
                            onClick()
                            action.actionUnit.invoke()
                        }
                    }
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

@Composable
private fun BackAction(
    backActionConfig: TopBarBackAction? = null,
    onBack: () -> Unit
) {
    val icon = backActionConfig?.iconResId ?: R.drawable.ic_back
    val description = backActionConfig?.descriptionResId ?: R.string.back

    IconButton(onClick = onBack) {
        Icon(
            painter = painterResource(icon),
            contentDescription = stringResource(description)
        )
    }
}