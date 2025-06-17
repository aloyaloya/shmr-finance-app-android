package com.example.shmr_finance_app_android.ui.screens.expenses_history_screen

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
import com.example.shmr_finance_app_android.domain.model.Expense
import com.example.shmr_finance_app_android.domain.model.ui.ListItem
import com.example.shmr_finance_app_android.domain.model.ui.MainContent
import com.example.shmr_finance_app_android.domain.model.ui.TrailContent
import com.example.shmr_finance_app_android.navigation.config.ScreenConfig
import com.example.shmr_finance_app_android.navigation.config.TopBarAction
import com.example.shmr_finance_app_android.navigation.config.TopBarConfig
import com.example.shmr_finance_app_android.navigation.routes.Route
import com.example.shmr_finance_app_android.ui.components.ListItemCard

@Composable
fun ExpensesHistoryScreen(
    viewModel: ExpensesHistoryScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsState()

    LaunchedEffect(updateConfigState) {
        updateConfigState(
            ScreenConfig(
                route = Route.ExpensesSubScreens.ExpensesHistory.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.expenses_history_screen_title,
                    showBackButton = true,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_calendar,
                        descriptionResId = R.string.expenses_analysis_description,
                        actionRoute = Route.ExpensesSubScreens.ExpensesHistory
                    )
                )
            )
        )
    }

    when (state) {
        is ExpensesHistoryScreenState.Loading -> HistoryLoadingState()
        is ExpensesHistoryScreenState.Error -> HistoryErrorState(
            message = (state as ExpensesHistoryScreenState.Error).message,
            onRetry = (state as ExpensesHistoryScreenState.Error).retryAction
        )
        is ExpensesHistoryScreenState.Empty -> HistoryEmptyState()
        is ExpensesHistoryScreenState.Success -> HistorySuccessState(
            expenses = (state as ExpensesHistoryScreenState.Success).expenses,
            totalAmount = (state as ExpensesHistoryScreenState.Success).totalAmount,
            startDate = (state as ExpensesHistoryScreenState.Success).startDate,
            endDate = (state as ExpensesHistoryScreenState.Success).endDate
        )
    }
}

@Composable
private fun HistorySuccessState(
    expenses: List<Expense>,
    totalAmount: Int,
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
            items(expenses, key = { expense -> expense.id }) { expense ->
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