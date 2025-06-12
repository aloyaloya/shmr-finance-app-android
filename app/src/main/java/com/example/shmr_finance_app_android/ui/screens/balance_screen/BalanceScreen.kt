package com.example.shmr_finance_app_android.ui.screens.balance_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import com.example.shmr_finance_app_android.data.model.domain.Balance
import com.example.shmr_finance_app_android.ui.screens.balance_screen.components.BalanceAmountCard
import com.example.shmr_finance_app_android.ui.screens.balance_screen.components.BalanceCurrencyCard
import com.example.shmr_finance_app_android.ui.viewmodels.BalanceScreenState
import com.example.shmr_finance_app_android.ui.viewmodels.BalanceScreenViewModel
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarState
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarViewModel

@Composable
fun BalanceScreen(
    topBarViewModel: TopBarViewModel,
    viewModel: BalanceScreenViewModel = viewModel() // пока не дошли до DI - вью модель здесь
) {
    val state by viewModel.screenState.collectAsState()

    val title = stringResource(R.string.balance_screen_title)
    val description = stringResource(R.string.balance_edit_description)
    LaunchedEffect(Unit) {
        topBarViewModel.update(
            TopBarState(
                title = title,
                actionIcon = R.drawable.ic_edit,
                actionDescription = description,
                onActionClick = { } // Переход на экран редактирования счета
            )
        )
    }

    when (state) {
        is BalanceScreenState.Loading -> BalanceLoadingState()
        is BalanceScreenState.Error -> BalanceErrorState(
            message = (state as BalanceScreenState.Error).message,
            onRetry = (state as BalanceScreenState.Error).retryAction
        )
        is BalanceScreenState.Success -> BalanceSuccessState(
            balance = (state as BalanceScreenState.Success).balance
        )
    }
}

@Composable
private fun BalanceSuccessState(
    balance: Balance
) {
    Column(Modifier.fillMaxSize()) {
        BalanceAmountCard(
            balanceAmount = balance.balanceFormatted,
            emoji = "💰",
            onClick = {  } // Переход на экран редактирования счета?
        )
        HorizontalDivider()
        BalanceCurrencyCard(
            balanceCurrency = balance.currency,
            onClick = {  } // Открытие модалки изменения валюты
        )
    }
}

@Composable
private fun BalanceLoadingState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
    }
}

@Composable
private fun BalanceErrorState(
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