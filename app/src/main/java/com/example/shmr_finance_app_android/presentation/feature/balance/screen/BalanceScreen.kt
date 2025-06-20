package com.example.shmr_finance_app_android.presentation.feature.balance.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent
import com.example.shmr_finance_app_android.presentation.feature.main.model.FloatingActionConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.balance.model.BalanceUiModel
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel.BalanceScreenState
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel.BalanceScreenViewModel

@Composable
fun BalanceScreen(
    viewModel: BalanceScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsState()

    LaunchedEffect(updateConfigState) {
        updateConfigState(
            ScreenConfig(
                route = Route.Root.Balance.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.balance_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_edit,
                        descriptionResId = R.string.balance_edit_description,
                        actionRoute = Route.Root.Balance.path
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = R.string.add_balance_description,
                    actionRoute = Route.Root.Balance.path
                )
            )
        )
    }

    when (state) {
        is BalanceScreenState.Loading -> BalanceLoadingState()
        is BalanceScreenState.Error -> BalanceErrorState(
            messageResId = (state as BalanceScreenState.Error).messageResId,
            onRetry = (state as BalanceScreenState.Error).retryAction
        )
        is BalanceScreenState.Success -> BalanceSuccessState(
            balance = (state as BalanceScreenState.Success).balance
        )
    }
}

@Composable
private fun BalanceSuccessState(
    balance: BalanceUiModel
) {
    Column(Modifier.fillMaxSize()) {
        ListItemCard(
            modifier = Modifier
                .clickable {  }
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                lead = LeadContent(text = "💰", color = MaterialTheme.colorScheme.background),
                content = MainContent(title = stringResource(R.string.balance)),
                trail = TrailContent(text = balance.balanceFormatted)
            ),
            trailIcon = R.drawable.ic_arrow_right
        )
        ListItemCard(
            modifier = Modifier
                .clickable {  }
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.currency)),
                trail = TrailContent(text = balance.currency)
            ),
            trailIcon = R.drawable.ic_arrow_right,
            showDivider = false
        )
    }
}

@Composable
private fun BalanceLoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
private fun BalanceErrorState(
    messageResId: Int,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(messageResId),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(dimensionResource(R.dimen.large_spacer)))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(text = stringResource(R.string.retry))
        }
    }
}