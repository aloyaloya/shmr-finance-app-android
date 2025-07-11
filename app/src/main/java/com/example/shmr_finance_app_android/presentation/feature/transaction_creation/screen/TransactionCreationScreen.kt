package com.example.shmr_finance_app_android.presentation.feature.transaction_creation.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionCreationScreenState
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionCreationViewModel
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionField
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionState
import com.example.shmr_finance_app_android.presentation.shared.components.AnimatedErrorSnackbar
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TransactionCreationScreen(
    viewModel: TransactionCreationViewModel = daggerViewModel(),
    isIncome: Boolean,
    updateConfigState: (ScreenConfig) -> Unit,
    onBackNavigate: () -> Unit
) {
    val state by viewModel.screenState.collectAsStateWithLifecycle()

    val isSuccess by viewModel.isSuccess.collectAsStateWithLifecycle()

    val availableCategories by viewModel.availableCategories
        .collectAsStateWithLifecycle(emptyList())

    val isSnackBarVisible by viewModel.snackBarVisible.collectAsStateWithLifecycle()
    val snackBarMessage by viewModel.snackBarMessage.collectAsStateWithLifecycle()

    val isDatePickerModalVisible by viewModel.datePickerModalVisible.collectAsStateWithLifecycle()
    val isTimePickerModalVisible by viewModel.timePickerModalVisible.collectAsStateWithLifecycle()

    val isCategorySelectionSheetVisible by viewModel
        .categoriesSelectionSheetVisible.collectAsStateWithLifecycle()

    val screenTitle = if (isIncome) {
        R.string.income_transaction_screen_title
    } else {
        R.string.expense_transaction_screen_title
    }

    if (isSuccess) {
        LaunchedEffect(Unit) {
            onBackNavigate()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.setTransactionsType(isIncome)
        viewModel.initialize()
        updateConfigState(
            ScreenConfig(
                topBarConfig = TopBarConfig(
                    titleResId = screenTitle,
                    backAction = TopBarBackAction(
                        iconResId = R.drawable.ic_cancel,
                        descriptionResId = R.string.edit_cancel_description,
                        actionUnit = onBackNavigate
                    ),
                    action = TopBarAction(
                        iconResId = R.drawable.ic_save,
                        descriptionResId = R.string.edit_save_description,
                        isActive = { viewModel.validateTransactionData() },
                        actionUnit = { viewModel.createTransaction() }
                    )
                )
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(Modifier.fillMaxSize()) {
            when (state) {
                is TransactionCreationScreenState.Loading -> LoadingState()
                is TransactionCreationScreenState.Error -> ErrorState(
                    messageResId = (state as TransactionCreationScreenState.Error).messageResId,
                    onRetry = (state as TransactionCreationScreenState.Error).retryAction
                )

                is TransactionCreationScreenState.Active -> TransactionCreationContent(
                    state = (state as TransactionCreationScreenState.Active).transaction,
                    onFieldChanged = { field, value -> viewModel.onFieldChanged(field, value) },
                    onCategoryClick = { viewModel.showCategoryBottomSheet() },
                    onDateClick = { viewModel.showDatePickerModal() },
                    onTimeClick = { viewModel.showTimePickerModal() }
                )
            }
        }

        AnimatedErrorSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            isVisible = isSnackBarVisible,
            messageResId = snackBarMessage,
            onDismiss = { viewModel.dismissSnackBar() }
        )
    }

    if (isCategorySelectionSheetVisible) {
        CategorySelectionSheet(
            items = availableCategories,
            onItemSelected = { viewModel.onFieldChanged(TransactionField.CATEGORY, it) },
            onDismiss = { viewModel.hideCategoryBottomSheet() }
        )
    }

    if (isDatePickerModalVisible) {
        DatePickerModal(
            onDateSelected = { viewModel.onFieldChanged(TransactionField.DATE, it) },
            onDismiss = { viewModel.hideDatePickerModal() }
        )
    }

    if (isTimePickerModalVisible) {
        TimePickerModal(
            onTimeSelected = { hours, minutes ->
                viewModel.onFieldChanged(TransactionField.TIME, Pair(hours, minutes))
            },
            onDismiss = { viewModel.hideTimePickerModal() }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun TransactionCreationContent(
    modifier: Modifier = Modifier,
    state: TransactionState,
    onFieldChanged: (TransactionField, Any) -> Unit,
    onCategoryClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Column(modifier.fillMaxSize()) {
        ListItemCard(
            modifier = Modifier.height(70.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.account)),
                trail = TrailContent(text = state.balance)
            )
        )
        ListItemCard(
            modifier = Modifier
                .clickable { onCategoryClick() }
                .height(70.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.category)),
                trail = TrailContent(text = state.categoryName)
            ),
            trailIcon = R.drawable.ic_arrow_right
        )
        ListItemCard(
            modifier = Modifier
                .clickable { onDateClick() }
                .height(70.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.date)),
                trail = TrailContent(text = state.date)
            ),
            trailIcon = R.drawable.ic_arrow_right
        )
        ListItemCard(
            modifier = Modifier
                .clickable { onTimeClick() }
                .height(70.dp),
            item = ListItem(
                content = MainContent(title = stringResource(R.string.time)),
                trail = TrailContent(text = state.time)
            ),
            trailIcon = R.drawable.ic_arrow_right
        )
        EditorTextField(
            modifier = Modifier.height(70.dp),
            value = state.amount,
            prefix = stringResource(R.string.amount),
            suffix = state.currencySymbol,
            placeholder = "0",
            keyboardType = KeyboardType.Number,
            onChange = { onFieldChanged(TransactionField.AMOUNT, it) }
        )
        EditorTextField(
            modifier = Modifier.height(70.dp),
            value = state.comment,
            textAlign = TextAlign.Left,
            placeholder = stringResource(R.string.comment_placeholder),
            placeholderAlign = TextAlign.Left,
            onChange = { onFieldChanged(TransactionField.COMMENT, it) }
        )
    }
}