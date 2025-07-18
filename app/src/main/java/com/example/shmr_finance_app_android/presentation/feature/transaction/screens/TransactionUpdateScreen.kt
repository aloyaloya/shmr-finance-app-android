package com.example.shmr_finance_app_android.presentation.feature.transaction.screens

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.di.daggerViewModel
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarBackAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateEvent
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateField
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateModal
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateUiState
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateViewModel
import com.example.shmr_finance_app_android.presentation.shared.components.AnimatedSnackbar
import com.example.shmr_finance_app_android.presentation.shared.components.CategorySelectionSheet
import com.example.shmr_finance_app_android.presentation.shared.components.DatePickerModal
import com.example.shmr_finance_app_android.presentation.shared.components.EditorTextField
import com.example.shmr_finance_app_android.presentation.shared.components.ErrorState
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.components.LoadingState
import com.example.shmr_finance_app_android.presentation.shared.components.TimePickerModal
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

@Composable
fun TransactionUpdateScreen(
    viewModel: TransactionUpdateViewModel = daggerViewModel(),
    transactionId: String,
    isIncome: Boolean,
    updateConfigState: (ScreenConfig) -> Unit,
    onBackNavigate: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var snackbarMsg by remember { mutableIntStateOf(0) }
    var snackbarVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TransactionUpdateEvent.ShowSnackBar -> {
                    snackbarMsg = event.messageResId
                    snackbarVisible = true
                }

                TransactionUpdateEvent.NavigateBack -> onBackNavigate()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.init(transactionId.toInt(), isIncome)
    }

    LaunchedEffect(uiState) {
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(
                    titleResId = if (isIncome) {
                        R.string.income_transaction_screen_title
                    } else {
                        R.string.expense_transaction_screen_title
                    },
                    backAction = TopBarBackAction(
                        iconResId = R.drawable.ic_cancel,
                        descriptionResId = R.string.edit_cancel_description,
                        actionUnit = onBackNavigate
                    ),
                    action = TopBarAction(
                        iconResId = R.drawable.ic_save,
                        descriptionResId = R.string.edit_save_description,
                        actionUnit = { viewModel.saveTransaction(transactionId.toInt()) }
                    )
                )
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            TransactionUpdateUiState.Loading -> LoadingState()
            is TransactionUpdateUiState.Error -> ErrorState(
                messageResId = (uiState as TransactionUpdateUiState.Error).messageResId,
                onRetry = { viewModel.init(transactionId.toInt(), isIncome) }
            )

            is TransactionUpdateUiState.Content -> TransactionUpdateContent(
                state = uiState as TransactionUpdateUiState.Content,
                onFieldChanged = viewModel::onFieldChanged,
                onCategoryClick = { viewModel.openModal(TransactionUpdateModal.CategoryPicker) },
                onDateClick = { viewModel.openModal(TransactionUpdateModal.DatePicker) },
                onTimeClick = { viewModel.openModal(TransactionUpdateModal.TimePicker) },
                onModalDismiss = viewModel::closeModal,
                onDeleteTransaction = { viewModel.deleteTransaction(transactionId.toInt()) }
            )
        }

        AnimatedSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            isVisible = snackbarVisible,
            messageResId = snackbarMsg,
            onDismiss = { snackbarVisible = false }
        )
    }
}

@Composable
private fun TransactionUpdateContent(
    modifier: Modifier = Modifier,
    state: TransactionUpdateUiState.Content,
    onFieldChanged: (TransactionUpdateField, Any) -> Unit,
    onCategoryClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onModalDismiss: () -> Unit,
    onDeleteTransaction: () -> Unit
) {
    val form = state.form
    val deleteText = if (form.isIncome) {
        R.string.delete_income_transaction
    } else {
        R.string.delete_expense_transaction
    }

    Column(modifier.fillMaxSize()) {
        ListItemCard(
            modifier = Modifier.height(70.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.account)),
                trail = TrailContent(text = form.balance)
            )
        )
        ListItemCard(
            modifier = Modifier
                .clickable { onCategoryClick() }
                .height(70.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.category)),
                trail = TrailContent(text = form.selectedCategory?.name ?: "")
            ),
            trailIcon = R.drawable.ic_arrow_right
        )
        ListItemCard(
            modifier = Modifier
                .clickable { onDateClick() }
                .height(70.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.date)),
                trail = TrailContent(text = form.date)
            ),
            trailIcon = R.drawable.ic_arrow_right
        )
        ListItemCard(
            modifier = Modifier
                .clickable { onTimeClick() }
                .height(70.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.time)),
                trail = TrailContent(text = form.time)
            ),
            trailIcon = R.drawable.ic_arrow_right
        )
        EditorTextField(
            modifier = Modifier.height(70.dp),
            value = form.amount,
            prefix = stringResource(R.string.amount),
            suffix = form.currencySymbol,
            placeholder = "0",
            keyboardType = KeyboardType.Number,
            onChange = { onFieldChanged(TransactionUpdateField.AMOUNT, it) }
        )
        EditorTextField(
            modifier = Modifier.height(70.dp),
            value = form.comment,
            textAlign = TextAlign.Left,
            placeholder = stringResource(R.string.comment_placeholder),
            placeholderAlign = TextAlign.Left,
            onChange = { onFieldChanged(TransactionUpdateField.COMMENT, it) }
        )

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_spacer)))

        TextButton(
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.medium_padding))
                .fillMaxWidth(),
            onClick = onDeleteTransaction,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text(
                text = stringResource(deleteText),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }

    when (state.visibleModal) {
        TransactionUpdateModal.CategoryPicker -> CategorySelectionSheet(
            items = state.categories,
            onItemSelected = {
                onFieldChanged(TransactionUpdateField.CATEGORY, it)
                onModalDismiss()
            },
            onDismiss = onModalDismiss
        )

        TransactionUpdateModal.DatePicker -> DatePickerModal(
            onDateSelected = {
                onFieldChanged(TransactionUpdateField.DATE, it)
                onModalDismiss()
            },
            onDismiss = onModalDismiss
        )

        TransactionUpdateModal.TimePicker -> TimePickerModal(
            onTimeSelected = { h, m ->
                onFieldChanged(TransactionUpdateField.TIME, Pair(h, m))
                onModalDismiss()
            },
            onDismiss = onModalDismiss
        )

        null -> Unit
    }
}