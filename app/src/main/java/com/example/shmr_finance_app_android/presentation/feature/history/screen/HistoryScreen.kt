package com.example.shmr_finance_app_android.presentation.feature.history.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.history.model.TransactionUiModel
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.feature.history.viewmodel.HistoryScreenState
import com.example.shmr_finance_app_android.presentation.feature.history.viewmodel.HistoryScreenViewModel

@Composable
fun HistoryScreen(
    viewModel: HistoryScreenViewModel = hiltViewModel(),
    isIncome: Boolean,
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsState()
    viewModel.setHistoryTransactionsType(isIncome)

    LaunchedEffect(updateConfigState) {
        updateConfigState(
            ScreenConfig(
                route = Route.SubScreens.History.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.expenses_history_screen_title,
                    showBackButton = true,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_calendar,
                        descriptionResId = R.string.expenses_analysis_description,
                        actionRoute = Route.SubScreens.History.route(income = isIncome)
                    )
                )
            )
        )
    }

    when (state) {
        is HistoryScreenState.Loading -> HistoryLoadingState()
        is HistoryScreenState.Error -> HistoryErrorState(
            message = (state as HistoryScreenState.Error).message,
            onRetry = (state as HistoryScreenState.Error).retryAction
        )
        is HistoryScreenState.Empty -> HistoryEmptyState()
        is HistoryScreenState.Success -> HistorySuccessState(
            transactions = (state as HistoryScreenState.Success).transactions,
            totalAmount = (state as HistoryScreenState.Success).totalAmount,
            startDate = (state as HistoryScreenState.Success).startDate,
            endDate = (state as HistoryScreenState.Success).endDate
        )
    }
}

@Composable
private fun HistorySuccessState(
    transactions: List<TransactionUiModel>,
    totalAmount: String,
    startDate: String,
    endDate: String
) {
    Column(Modifier.fillMaxSize()) {
        LazyColumn {
            item {
                ListItemCard(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                        .height(56.dp),
                    item = ListItem(
                        content = MainContent(title = stringResource(R.string.period_start)),
                        trail = TrailContent(text = startDate)
                    )
                )
                ListItemCard(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                        .height(56.dp),
                    item = ListItem(
                        content = MainContent(title = stringResource(R.string.period_end)),
                        trail = TrailContent(text = endDate)
                    )
                )
                ListItemCard(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                        .height(56.dp),
                    showDivider = false,
                    item = ListItem(
                        content = MainContent(title = stringResource(R.string.summary)),
                        trail = TrailContent(text = totalAmount.toString())
                    )
                )
            }
            items(transactions, key = { transaction -> transaction.id }) { expense ->
                ListItemCard(
                    modifier = Modifier
                        .clickable { }
                        .height(70.dp),
                    item = expense.toListItem(),
                    trailIcon = R.drawable.ic_arrow_right,
                    subtitleStyle = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Composable
private fun HistoryLoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
private fun HistoryErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
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

@Composable
private fun HistoryEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_expenses_found),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}