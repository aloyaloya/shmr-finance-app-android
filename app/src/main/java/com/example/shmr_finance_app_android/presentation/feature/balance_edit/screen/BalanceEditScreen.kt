package com.example.shmr_finance_app_android.presentation.feature.balance_edit.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.balance_edit.component.CurrencySelectionSheet
import com.example.shmr_finance_app_android.presentation.feature.balance_edit.component.EditorTextField
import com.example.shmr_finance_app_android.presentation.feature.balance_edit.model.CurrencyItem
import com.example.shmr_finance_app_android.presentation.feature.balance_edit.viewmodel.BalanceEditScreenState
import com.example.shmr_finance_app_android.presentation.feature.balance_edit.viewmodel.BalanceEditScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarBackAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

@Composable
fun BalanceEditScreen(
    viewModel: BalanceEditScreenViewModel = hiltViewModel(),
    balanceId: String,
    updateConfigState: (ScreenConfig) -> Unit
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()
    val isCurrencySelectionSheetVisible by viewModel
        .currencySelectionSheetVisible.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.setAccountId(balanceId)
        updateConfigState(
            ScreenConfig(
                route = Route.SubScreens.BalanceEdit.path,
                topBarConfig = TopBarConfig(
                    titleResId = R.string.balance_screen_title,
                    showBackButton = true,
                    backAction = TopBarBackAction(
                        iconResId = R.drawable.ic_cancel,
                        descriptionResId = R.string.balance_edit_cancel_description
                    ),
                    action = TopBarAction(
                        iconResId = R.drawable.ic_save,
                        descriptionResId = R.string.balance_edit_save_description,
                        actionRoute = Route.Root.Balance.path,
                        actionUnit = { viewModel.updateAccountData() }
                    )
                )
            )
        )
    }

    Column(Modifier.fillMaxSize()) {
        when (state) {
            is BalanceEditScreenState.Loading -> LoadingState()
            is BalanceEditScreenState.Error -> ErrorState(
                messageResId = (state as BalanceEditScreenState.Error).messageResId,
                onRetry = (state as BalanceEditScreenState.Error).retryAction
            )

            is BalanceEditScreenState.Success -> BalanceEditContent(
                state = state as BalanceEditScreenState.Success,
                onNameChanged = { viewModel.onNameEdited(it) },
                onBalanceChanged = { viewModel.onBalanceEdited(it) },
                onCurrencyClick = { viewModel.showCurrencyBottomSheet() }
            )
        }
    }

    if (isCurrencySelectionSheetVisible) {
        CurrencySelectionSheet(
            items = CurrencyItem.items,
            onItemSelected = { viewModel.onCurrencySelected(it) },
            onDismiss = { viewModel.hideCurrencyBottomSheet() }
        )
    }
}

@Composable
private fun BalanceEditContent(
    modifier: Modifier = Modifier,
    state: BalanceEditScreenState.Success,
    onNameChanged: (String) -> Unit,
    onBalanceChanged: (String) -> Unit,
    onCurrencyClick: () -> Unit
) {
    val name by state.name.collectAsState()
    val balance by state.balance.collectAsState()
    val currencySymbol by state.currencySymbol.collectAsState()

    Column(modifier.fillMaxSize()) {
        EditorTextField(
            value = name,
            prefixResId = R.string.balance_name,
            onChange = onNameChanged,
        )

        EditorTextField(
            value = balance,
            prefixResId = R.string.balance,
            suffix = currencySymbol,
            onChange = onBalanceChanged,
            keyboardType = KeyboardType.Number
        )

        ListItemCard(
            modifier = Modifier
                .clickable { onCurrencyClick() }
                .height(56.dp),
            item = ListItem(
                content = MainContent(
                    title = stringResource(R.string.currency),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                trail = TrailContent(text = currencySymbol)
            ),
            trailIcon = R.drawable.ic_arrow_right,
        )
    }
}