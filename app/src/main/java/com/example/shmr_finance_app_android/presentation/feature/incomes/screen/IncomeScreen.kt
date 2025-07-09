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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.incomes.model.IncomeUiModel
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomeScreenState
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomeScreenViewModel
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
    viewModel: IncomeScreenViewModel = hiltViewModel(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.initialize()
        updateConfigState(
            ScreenConfig(
                route = Route.Root.Income.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.income_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_history,
                        descriptionResId = R.string.incomes_history_description,
                        actionRoute = Route.SubScreens.History.route(income = true)
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = R.string.add_income_description,
                    actionRoute = Route.Root.Income.path
                )
            )
        )
    }

    when (state) {
        is IncomeScreenState.Loading -> LoadingState()
        is IncomeScreenState.Error -> ErrorState(
            messageResId = (state as IncomeScreenState.Error).messageResId,
            onRetry = (state as IncomeScreenState.Error).retryAction
        )

        is IncomeScreenState.Empty -> EmptyState(
            messageResId = R.string.today_no_income_found
        )

        is IncomeScreenState.Success -> IncomeSuccessState(
            incomes = (state as IncomeScreenState.Success).incomes,
            totalAmount = (state as IncomeScreenState.Success).totalAmount
        )
    }
}

@Composable
private fun IncomeSuccessState(
    incomes: List<IncomeUiModel>,
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
            items(incomes, key = { income -> income.id }) { income ->
                ListItemCard(
                    modifier = Modifier
                        .clickable { } // Переход пока не понятно куда
                        .height(70.dp),
                    item = income.toListItem(),
                    trailIcon = R.drawable.ic_arrow_right
                )
            }
        }
    }
}