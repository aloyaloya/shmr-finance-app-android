package com.example.shmr_finance_app_android.presentation.feature.analytic.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.di.daggerViewModel
import com.example.shmr_finance_app_android.presentation.feature.analytic.component.MonthSelectionHeader
import com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel.AnalyticModal
import com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel.AnalyticUiState
import com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel.AnalyticViewModel
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarBackAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.shared.components.DatePickerModal
import com.example.shmr_finance_app_android.presentation.shared.components.EmptyState
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

@Composable
fun AnalyticScreen(
    viewModel: AnalyticViewModel = daggerViewModel(),
    isIncome: Boolean,
    updateConfigState: (ScreenConfig) -> Unit,
    onBackNavigate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val visibleModal by viewModel.visibleModal.collectAsStateWithLifecycle()

    val startMonth by viewModel.startMonth.collectAsStateWithLifecycle()
    val endMonth by viewModel.endMonth.collectAsStateWithLifecycle()

    val emptyMessage = when (isIncome) {
        true -> R.string.period_no_income_found
        false -> R.string.period_no_expenses_found
    }

    LaunchedEffect(Unit) { viewModel.init(isIncome) }

    LaunchedEffect(Unit) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(
                    titleResId = R.string.analytic_screen_title,
                    backAction = TopBarBackAction(
                        actionUnit = onBackNavigate
                    )
                )
            )
        )
    }

    Column(Modifier.fillMaxSize()) {
        MonthSelectionHeader(
            startMonth = startMonth,
            onStartMonth = { viewModel.openModal(AnalyticModal.StartDatePicker) },
            endMonth = endMonth,
            onEndMonth = { viewModel.openModal(AnalyticModal.EndDatePicker) }
        )
        when (uiState) {
            AnalyticUiState.Loading -> LoadingState()
            is AnalyticUiState.Error -> ErrorState(
                messageResId = (uiState as AnalyticUiState.Error).messageResId,
                onRetry = viewModel::loadData
            )

            is AnalyticUiState.Empty -> EmptyState(
                messageResId = emptyMessage
            )

            is AnalyticUiState.Content -> AnalyticContentState(
                state = uiState as AnalyticUiState.Content
            )
        }
    }

    when (visibleModal) {
        AnalyticModal.StartDatePicker -> DatePickerModal(
            onDateSelected = {
                viewModel.onMonthSelected(AnalyticModal.StartDatePicker, it)
                viewModel.closeModal()
            },
            onDismiss = viewModel::closeModal
        )

        AnalyticModal.EndDatePicker -> DatePickerModal(
            onDateSelected = {
                viewModel.onMonthSelected(AnalyticModal.EndDatePicker, it)
                viewModel.closeModal()
            },
            onDismiss = viewModel::closeModal
        )

        null -> Unit
    }
}

@Composable
private fun AnalyticContentState(
    state: AnalyticUiState.Content
) {
    val categories = state.analyticItems

    Column(Modifier.fillMaxSize()) {
        ListItemCard(
            modifier = Modifier.height(56.dp),
            showDivider = true,
            item = ListItem(
                content = MainContent(title = stringResource(R.string.summary)),
                trail = TrailContent(text = state.totalAmount)
            )
        )
        LazyColumn {
            items(categories, key = { category -> category.categoryId }) { category ->
                ListItemCard(
                    modifier = Modifier.height(70.dp),
                    item = category.toListItem()
                )
            }
        }
    }
}