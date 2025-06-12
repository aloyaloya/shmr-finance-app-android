package com.example.shmr_finance_app_android.ui.screens.income_screen

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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.mapper.toListItem
import com.example.shmr_finance_app_android.data.model.domain.Income
import com.example.shmr_finance_app_android.ui.components.ListItemCard
import com.example.shmr_finance_app_android.ui.components.TotalAmountCard
import com.example.shmr_finance_app_android.ui.viewmodels.IncomeScreenState
import com.example.shmr_finance_app_android.ui.viewmodels.IncomeScreenViewModel
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarState
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarViewModel

@Composable
fun IncomeScreen(
    topBarViewModel: TopBarViewModel,
    viewModel: IncomeScreenViewModel = viewModel() // пока не дошли до DI - вью модель здесь
) {
    val state by viewModel.screenState.collectAsState()

    val title = stringResource(R.string.income_screen_title)
    val description = stringResource(R.string.history_description)
    LaunchedEffect(Unit) {
        topBarViewModel.update(
            TopBarState(
                title = title,
                actionIcon = R.drawable.ic_history,
                actionDescription = description,
                onActionClick = { } // Переход на экран Моя история
            )
        )
    }

    when (state) {
        is IncomeScreenState.Loading -> IncomeLoadingState()
        is IncomeScreenState.Error -> IncomeErrorState(
            message = (state as IncomeScreenState.Error).message,
            onRetry = (state as IncomeScreenState.Error).retryAction
        )
        is IncomeScreenState.Empty -> IncomeEmptyState()
        is IncomeScreenState.Success -> IncomeSuccessState(
            incomes = (state as IncomeScreenState.Success).incomes
        )
    }
}

@Composable
private fun IncomeSuccessState(
    incomes: List<Income>
) {
    Column(Modifier.fillMaxSize()) {
        TotalAmountCard(totalAmount = "600 000 ₽")
        LazyColumn {
            items(incomes) { income ->
                ListItemCard(
                    item = income.toListItem(),
                    trailIcon = R.drawable.ic_arrow_right,
                    onClick = { } // Переход пока не понятно куда
                )
            }
        }
    }
}

@Composable
private fun IncomeLoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
private fun IncomeErrorState(
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
private fun IncomeEmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_income_found),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}