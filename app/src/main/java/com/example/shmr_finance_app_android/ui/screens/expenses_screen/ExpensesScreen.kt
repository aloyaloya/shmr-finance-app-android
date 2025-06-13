package com.example.shmr_finance_app_android.ui.screens.expenses_screen

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.mapper.toListItem
import com.example.shmr_finance_app_android.data.model.domain.Expense
import com.example.shmr_finance_app_android.ui.components.ListItemCard
import com.example.shmr_finance_app_android.ui.components.TotalAmountCard
import com.example.shmr_finance_app_android.ui.viewmodels.ExpensesScreenState
import com.example.shmr_finance_app_android.ui.viewmodels.ExpensesScreenViewModel

@Composable
fun ExpensesScreen(
    viewModel: ExpensesScreenViewModel = viewModel() // пока не дошли до DI - вью модель здесь
) {
    val state by viewModel.screenState.collectAsState()

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
        TotalAmountCard(totalAmount = "436 558 ₽")
        LazyColumn {
            items(expenses, key = { expense -> expense.id }) { expense ->
                ListItemCard(
                    item = expense.toListItem(),
                    trailIcon = R.drawable.ic_arrow_right,
                    onClick = { } // Переход на экран Мои расходы
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