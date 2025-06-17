package com.example.shmr_finance_app_android.ui.screens.balance_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.model.domain.Balance
import com.example.shmr_finance_app_android.data.model.ui.LeadContent
import com.example.shmr_finance_app_android.data.model.ui.ListItem
import com.example.shmr_finance_app_android.data.model.ui.MainContent
import com.example.shmr_finance_app_android.data.model.ui.TrailContent
import com.example.shmr_finance_app_android.navigation.config.FloatingActionConfig
import com.example.shmr_finance_app_android.navigation.config.ScreenConfig
import com.example.shmr_finance_app_android.navigation.config.TopBarAction
import com.example.shmr_finance_app_android.navigation.config.TopBarConfig
import com.example.shmr_finance_app_android.navigation.routes.Route
import com.example.shmr_finance_app_android.ui.components.ListItemCard

@Composable
fun BalanceScreen(
    viewModel: BalanceScreenViewModel = viewModel(), // пока не дошли до DI - вью модель здесь
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsState()

    LaunchedEffect(updateConfigState) {
        updateConfigState(
            ScreenConfig(
                route = Route.Root.Balance.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.balance_screen_title,
                    action = TopBarAction(
                        iconResId = R.drawable.ic_edit,
                        descriptionResId = R.string.balance_edit_description,
                        actionRoute = Route.Root.Balance
                    )
                ),
                floatingActionConfig = FloatingActionConfig(
                    descriptionResId = R.string.add_balance_description,
                    actionRoute = Route.Root.Balance
                )
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
        ListItemCard(
            modifier = Modifier
                .clickable {  }
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                lead = LeadContent(text = "💰", color = MaterialTheme.colorScheme.background),
                content = MainContent(title = stringResource(R.string.balance)),
                trail = TrailContent(text = "-600 000 ₽")
            ),
            trailIcon = R.drawable.ic_arrow_right
        )
        ListItemCard(
            modifier = Modifier
                .clickable {  }
                .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
                .height(56.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.currency)),
                trail = TrailContent(text = balance.currency)
            ),
            trailIcon = R.drawable.ic_arrow_right,
            noDivider = true
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