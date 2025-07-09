package com.example.shmr_finance_app_android.presentation.feature.balance.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.balance.model.BalanceUiModel
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel.BalanceScreenState
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel.BalanceScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.main.model.FloatingActionConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

@Composable
fun BalanceScreen(
    viewModel: BalanceScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initialize()
        updateConfigState(
            ScreenConfig(
                route = Route.Root.Balance.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.balance_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_edit,
                        descriptionResId = R.string.balance_edit_description,
                        actionRoute = Route.SubScreens.BalanceEdit.route(viewModel.getAccountId())
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
        is BalanceScreenState.Loading -> LoadingState()
        is BalanceScreenState.Error -> ErrorState(
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
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                lead = LeadContent.Text(text = "💰", color = MaterialTheme.colorScheme.background),
                content = MainContent(title = stringResource(R.string.balance)),
                trail = TrailContent(text = balance.balanceFormatted)
            ),
        )
        ListItemCard(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.currency)),
                trail = TrailContent(text = balance.currency)
            ),
            showDivider = false
        )
    }
}