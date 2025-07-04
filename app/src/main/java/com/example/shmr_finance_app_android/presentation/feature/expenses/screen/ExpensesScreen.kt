package com.example.shmr_finance_app_android.presentation.feature.expenses.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.shmr_finance_app_android.presentation.feature.expenses.model.ExpenseUiModel
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesScreenState
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.main.model.FloatingActionConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.shared.components.EmptyState
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

@Composable
fun ExpensesScreen(
    viewModel: ExpensesScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit,
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                route = Route.Root.Expenses.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.expense_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_history,
                        descriptionResId = R.string.expenses_history_description,
                        actionRoute = Route.SubScreens.History.route(income = false)
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = R.string.add_expense_description,
                    actionRoute = Route.Root.Expenses.path
                )
            )
        )
    }

    when (state) {
        is ExpensesScreenState.Loading -> LoadingState()
        is ExpensesScreenState.Error -> ErrorState(
            messageResId = (state as ExpensesScreenState.Error).messageResId,
            onRetry = (state as ExpensesScreenState.Error).retryAction
        )

        is ExpensesScreenState.Empty -> EmptyState(
            messageResId = R.string.today_no_expenses_found
        )

        is ExpensesScreenState.Success -> ExpensesSuccessState(
            expenses = (state as ExpensesScreenState.Success).expenses,
            totalAmount = (state as ExpensesScreenState.Success).totalAmount
        )
    }
}

@Composable
private fun ExpensesSuccessState(
    expenses: List<ExpenseUiModel>,
    totalAmount: String
) {
    Column(Modifier.fillMaxSize()) {
        ListItemCard(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.total_amount)),
                trail = TrailContent(text = totalAmount)
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