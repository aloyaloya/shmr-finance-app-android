package com.example.shmr_finance_app_android.presentation.feature.balance_update.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.di.daggerViewModel
import com.example.shmr_finance_app_android.presentation.feature.balance_update.component.CurrencySelectionSheet
import com.example.shmr_finance_app_android.presentation.feature.balance_update.model.CurrencyItem
import com.example.shmr_finance_app_android.presentation.feature.balance_update.viewmodel.BalanceUpdateEvent
import com.example.shmr_finance_app_android.presentation.feature.balance_update.viewmodel.BalanceUpdateField
import com.example.shmr_finance_app_android.presentation.feature.balance_update.viewmodel.BalanceUpdateModal
import com.example.shmr_finance_app_android.presentation.feature.balance_update.viewmodel.BalanceUpdateScreenViewModel
import com.example.shmr_finance_app_android.presentation.feature.balance_update.viewmodel.BalanceUpdateUiState
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarBackAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.shared.components.AnimatedErrorSnackbar
import com.example.shmr_finance_app_android.presentation.shared.components.EditorTextField
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

@Composable
fun BalanceUpdateScreen(
    viewModel: BalanceUpdateScreenViewModel = daggerViewModel(),
    balanceId: String,
    updateConfigState: (ScreenConfig) -> Unit,
    onBackNavigate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var snackbarMsg by remember { mutableIntStateOf(0) }
    var snackbarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is BalanceUpdateEvent.ShowSnackBar -> {
                    snackbarMsg = event.messageResId
                    snackbarVisible = true
                }

                BalanceUpdateEvent.NavigateBack -> onBackNavigate()
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.init(balanceId) }

    LaunchedEffect(Unit, viewModel) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(
                    titleResId = R.string.balance_screen_title,
                    backAction = TopBarBackAction(
                        iconResId = R.drawable.ic_cancel,
                        descriptionResId = R.string.edit_cancel_description,
                        actionUnit = onBackNavigate
                    ),
                    action = TopBarAction(
                        iconResId = R.drawable.ic_save,
                        descriptionResId = R.string.edit_save_description,
                        actionUnit = { viewModel.saveBalance(balanceId) }
                    )
                )
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            BalanceUpdateUiState.Loading -> LoadingState()
            is BalanceUpdateUiState.Error -> ErrorState(
                messageResId = (uiState as BalanceUpdateUiState.Error).messageResId,
                onRetry = { viewModel.init(balanceId) }
            )

            is BalanceUpdateUiState.Content -> BalanceUpdateContent(
                state = uiState as BalanceUpdateUiState.Content,
                onFieldChanged = viewModel::onFieldChanged,
                onCurrencyClick = { viewModel.openModal(BalanceUpdateModal.CurrencyPicker) },
                onDeleteBalance = { }, // Удаление счета\
                onModalDismiss = viewModel::closeModal
            )
        }

        AnimatedErrorSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            isVisible = snackbarVisible,
            messageResId = snackbarMsg,
            onDismiss = { snackbarVisible = false }
        )
    }
}

@Composable
private fun BalanceUpdateContent(
    modifier: Modifier = Modifier,
    state: BalanceUpdateUiState.Content,
    onFieldChanged: (BalanceUpdateField, Any) -> Unit,
    onCurrencyClick: () -> Unit,
    onDeleteBalance: () -> Unit,
    onModalDismiss: () -> Unit
) {
    val form = state.form

    Column(modifier.fillMaxSize()) {
        EditorTextField(
            value = form.name,
            prefix = stringResource(R.string.balance_name),
            onChange = { onFieldChanged(BalanceUpdateField.NAME, it) },
        )

        EditorTextField(
            value = form.amount,
            prefix = stringResource(R.string.balance),
            suffix = form.currency?.currencySymbol ?: "₽",
            onChange = { onFieldChanged(BalanceUpdateField.AMOUNT, it) },
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
                trail = TrailContent(text = form.currency?.currencySymbol ?: "₽")
            ),
            trailIcon = R.drawable.ic_arrow_right,
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_spacer)))

        TextButton(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                .fillMaxWidth(),
            onClick = onDeleteBalance,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(
                text = stringResource(R.string.delete_balance),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }

    when (state.visibleModal) {
        BalanceUpdateModal.CurrencyPicker -> CurrencySelectionSheet(
            items = CurrencyItem.items,
            onItemSelected = { onFieldChanged(BalanceUpdateField.CURRENCY, it) },
            onDismiss = onModalDismiss
        )

        null -> Unit
    }
}