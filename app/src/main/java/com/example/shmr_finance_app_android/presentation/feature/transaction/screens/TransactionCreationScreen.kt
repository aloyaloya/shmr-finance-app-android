package com.example.shmr_finance_app_android.presentation.feature.transaction.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionCreationEvent
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionCreationField
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionCreationModal
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionCreationUiState
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionCreationViewModel
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
fun TransactionCreationScreen(
    viewModel: TransactionCreationViewModel = daggerViewModel(),
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
                is TransactionCreationEvent.ShowSnackBar -> {
                    snackbarMsg = event.messageResId
                    snackbarVisible = true
                }

                TransactionCreationEvent.NavigateBack -> onBackNavigate()
            }
        }
    }

    LaunchedEffect(Unit) { viewModel.init(isIncome) }

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
                        actionUnit = { viewModel.createTransaction() }
                    )
                )
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            TransactionCreationUiState.Loading -> LoadingState()
            is TransactionCreationUiState.Error -> ErrorState(
                messageResId = (uiState as TransactionCreationUiState.Error).messageResId,
                onRetry = { viewModel.init(isIncome) }
            )

            is TransactionCreationUiState.Content -> TransactionCreationContent(
                state = uiState as TransactionCreationUiState.Content,
                onFieldChanged = viewModel::onFieldChanged,
                onCategoryClick = { viewModel.openModal(TransactionCreationModal.CategoryPicker) },
                onDateClick = { viewModel.openModal(TransactionCreationModal.DatePicker) },
                onTimeClick = { viewModel.openModal(TransactionCreationModal.TimePicker) },
                onModalDismiss = viewModel::closeModal
            )
        }

        AnimatedSnackbar(
            modifier = Modifier.align(Alignment.TopCenter),
            isVisible = snackbarVisible,
            messageResId = snackbarMsg,
            onDismiss = { snackbarVisible = false }
        )
    }
}

@Composable
private fun TransactionCreationContent(
    modifier: Modifier = Modifier,
    state: TransactionCreationUiState.Content,
    onFieldChanged: (TransactionCreationField, Any) -> Unit,
    onCategoryClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit,
    onModalDismiss: () -> Unit
) {
    val form = state.form

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
            onChange = { onFieldChanged(TransactionCreationField.AMOUNT, it) }
        )
        EditorTextField(
            modifier = Modifier.height(70.dp),
            value = form.comment,
            textAlign = TextAlign.Left,
            placeholder = stringResource(R.string.comment_placeholder),
            placeholderAlign = TextAlign.Left,
            onChange = { onFieldChanged(TransactionCreationField.COMMENT, it) }
        )
    }

    when (state.visibleModal) {
        TransactionCreationModal.CategoryPicker -> CategorySelectionSheet(
            items = state.categories,
            onItemSelected = {
                onFieldChanged(TransactionCreationField.CATEGORY, it)
                onModalDismiss()
            },
            onDismiss = onModalDismiss
        )

        TransactionCreationModal.DatePicker -> DatePickerModal(
            onDateSelected = {
                onFieldChanged(TransactionCreationField.DATE, it)
                onModalDismiss()
            },
            onDismiss = onModalDismiss
        )

        TransactionCreationModal.TimePicker -> TimePickerModal(
            onTimeSelected = { h, m ->
                onFieldChanged(TransactionCreationField.TIME, Pair(h, m))
                onModalDismiss()
            },
            onDismiss = onModalDismiss
        )

        null -> Unit
    }
}