package com.example.shmr_finance_app_android.presentation.feature.incomes.screen

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
import com.example.shmr_finance_app_android.presentation.feature.incomes.model.IncomeUiModel
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomeScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomesUiState
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
fun IncomeScreen(
    viewModel: IncomeScreenViewModel = daggerViewModel(),
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
                    titleResId = R.string.income_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_history,
                        descriptionResId = R.string.incomes_history_description,
                        actionUnit = onHistoryNavigate
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = R.string.add_income_description,
                    actionUnit = onCreateNavigate
                )
            )
        )
    }

    when (uiState) {
        is IncomesUiState.Loading -> LoadingState()
        is IncomesUiState.Error -> ErrorState(
            messageResId = (uiState as IncomesUiState.Error).messageResId,
            onRetry = viewModel::init
        )

        is IncomesUiState.Empty -> EmptyState(
            messageResId = R.string.today_no_income_found
        )

        is IncomesUiState.Content -> IncomesContent(
            incomes = (uiState as IncomesUiState.Content).incomes,
            totalAmount = (uiState as IncomesUiState.Content).totalAmount,
            onTransactionUpdateNavigate = onTransactionUpdateNavigate
        )
    }
}

@Composable
private fun IncomesContent(
    incomes: List<IncomeUiModel>,
    totalAmount: String,
    onTransactionUpdateNavigate: (Int) -> Unit,
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
            items(incomes, key = { income -> income.id }) { income ->
                ListItemCard(
                    modifier = Modifier
                        .clickable { onTransactionUpdateNavigate(income.id) }
                        .height(70.dp),
                    item = income.toListItem(),
                    trailIcon = R.drawable.ic_arrow_right
                )
            }
        }
    }
}