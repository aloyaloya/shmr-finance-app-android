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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.di.daggerViewModel
import com.example.shmr_finance_app_android.presentation.feature.expenses.model.ExpenseUiModel
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesUiState
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
    viewModel: ExpensesScreenViewModel = daggerViewModel(),
    updateConfigState: (ScreenConfig) -> Unit,
    onHistoryNavigate: () -> Unit,
    onTransactionUpdateNavigate: (Int) -> Unit,
    onCreateNavigate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.init() }

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(
                    titleResId = R.string.expense_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_history,
                        descriptionResId = R.string.expenses_history_description,
                        actionUnit = onHistoryNavigate
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = R.string.add_expense_description,
                    actionUnit = onCreateNavigate
                )
            )
        )
    }

    when (uiState) {
        is ExpensesUiState.Loading -> LoadingState()
        is ExpensesUiState.Error -> ErrorState(
            messageResId = (uiState as ExpensesUiState.Error).messageResId,
            onRetry = viewModel::init
        )

        is ExpensesUiState.Empty -> EmptyState(
            messageResId = R.string.today_no_expenses_found
        )

        is ExpensesUiState.Content -> ExpensesContent(
            expenses = (uiState as ExpensesUiState.Content).expenses,
            totalAmount = (uiState as ExpensesUiState.Content).totalAmount,
            onTransactionUpdateNavigate = onTransactionUpdateNavigate
        )
    }
}

@Composable
private fun ExpensesContent(
    expenses: List<ExpenseUiModel>,
    totalAmount: String,
    onTransactionUpdateNavigate: (Int) -> Unit
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
                        .clickable { onTransactionUpdateNavigate(expense.id) }
                        .height(70.dp),
                    item = expense.toListItem(),
                    trailIcon = R.drawable.ic_arrow_right
                )
            }
        }
    }
}