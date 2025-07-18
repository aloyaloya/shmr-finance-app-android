package com.example.shmr_finance_app_android.presentation.feature.transaction.screens

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
import com.example.shmr_finance_app_android.presentation.feature.main.model.FloatingActionConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.feature.transaction.models.TransactionTodayModel
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionTodayViewModel
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionsTodayUiState
import com.example.shmr_finance_app_android.presentation.shared.components.EmptyState
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

@Composable
fun TransactionsTodayScreen(
    viewModel: TransactionTodayViewModel = daggerViewModel(),
    isIncome: Boolean,
    updateConfigState: (ScreenConfig) -> Unit,
    onHistoryNavigate: () -> Unit,
    onTransactionUpdateNavigate: (Int) -> Unit,
    onCreateNavigate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) { viewModel.init(isIncome) }

    LaunchedEffect(isIncome, onHistoryNavigate, onCreateNavigate) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(
                    titleResId = if (isIncome) {
                        R.string.income_screen_title
                    } else {
                        R.string.expense_screen_title
                    },
                    action = TopBarAction(
                        iconResId = R.drawable.ic_history,
                        descriptionResId = if (isIncome) {
                            R.string.incomes_history_description
                        } else {
                            R.string.expenses_history_description
                        },
                        actionUnit = onHistoryNavigate
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = if (isIncome) {
                        R.string.add_income_description
                    } else {
                        R.string.add_expense_description
                    },
                    actionUnit = onCreateNavigate
                )
            )
        )
    }

    when (uiState) {
        is TransactionsTodayUiState.Loading -> LoadingState()
        is TransactionsTodayUiState.Error -> ErrorState(
            messageResId = (uiState as TransactionsTodayUiState.Error).messageResId,
            onRetry = { viewModel.init(isIncome) }
        )

        is TransactionsTodayUiState.Empty -> EmptyState(
            messageResId = if (isIncome) {
                R.string.today_no_income_found
            } else {
                R.string.today_no_expenses_found
            }
        )

        is TransactionsTodayUiState.Content -> TransactionTodayContent(
            transactions = (uiState as TransactionsTodayUiState.Content).transactions,
            totalAmount = (uiState as TransactionsTodayUiState.Content).totalAmount,
            onTransactionUpdateNavigate = onTransactionUpdateNavigate
        )
    }
}

@Composable
private fun TransactionTodayContent(
    transactions: List<TransactionTodayModel>,
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
            items(transactions, key = { transaction -> transaction.id }) { transaction ->
                ListItemCard(
                    modifier = Modifier
                        .clickable { onTransactionUpdateNavigate(transaction.id) }
                        .height(70.dp),
                    item = transaction.toListItem(),
                    trailIcon = R.drawable.ic_arrow_right
                )
            }
        }
    }
}