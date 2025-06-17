package com.example.shmr_finance_app_android.ui.screens.expenses_screen

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
import com.example.shmr_finance_app_android.navigation.config.FloatingActionConfig
import com.example.shmr_finance_app_android.navigation.config.ScreenConfig
import com.example.shmr_finance_app_android.navigation.config.TopBarAction
import com.example.shmr_finance_app_android.navigation.config.TopBarConfig
import com.example.shmr_finance_app_android.navigation.routes.Route
import com.example.shmr_finance_app_android.ui.components.ListItemCard

@Composable
fun ExpensesScreen(
    viewModel: ExpensesScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit,
) {
    val state by viewModel.screenState.collectAsState()

    LaunchedEffect(updateConfigState) {
        updateConfigState(
            ScreenConfig(
                route = Route.Root.Expenses.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.expense_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_history,
                        descriptionResId = R.string.expenses_history_description,
                        actionRoute = Route.ExpensesSubScreens.ExpensesHistory
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = R.string.add_expense_description,
                    actionRoute = Route.Root.Expenses
                )
            )
        )
    }

    when (state) {
        is ExpensesScreenState.Loading -> ExpensesLoadingState()
        is ExpensesScreenState.Error -> ExpensesErrorState(
            message = (state as ExpensesScreenState.Error).message,
            onRetry = (state as ExpensesScreenState.Error).retryAction
        )
        is ExpensesScreenState.Empty -> ExpensesEmptyState()
        is ExpensesScreenState.Success -> ExpensesSuccessState(
            expenses = (state as ExpensesScreenState.Success).expenses
        )
    }
}

@Composable
private fun ExpensesSuccessState(
    expenses: List<Expense>
) {
    Column(Modifier.fillMaxSize()) {
        ListItemCard(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.total_amount)),
                trail = TrailContent(text = "436 558 ₽")
            )
        )
        LazyColumn {
            items(expenses, key = { expense -> expense.id }) { expense ->
                ListItemCard(
                    modifier = Modifier
                        .clickable { } // Переход на экран Мои расходы
                        .height(70.dp),
                    item = expense.toListItem(),
                    trailIcon = R.drawable.ic_arrow_right
                )
            }
        }
    }
}

@Composable
private fun ExpensesLoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
private fun ExpensesErrorState(
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
private fun ExpensesEmptyState() {
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