package com.example.shmr_finance_app_android.presentation.feature.history.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.formatHumanDateToIso
import com.example.shmr_finance_app_android.core.utils.formatLongToHumanDate
import com.example.shmr_finance_app_android.core.utils.getCurrentDateIso
import com.example.shmr_finance_app_android.core.utils.getStartOfCurrentMonth
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.history.mapper.TransactionToTransactionUiMapper
import com.example.shmr_finance_app_android.presentation.feature.history.model.TransactionUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface HistoryScreenState {
    data object Loading : HistoryScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : HistoryScreenState
    data object Empty : HistoryScreenState
    data class Success(
        val transactions: List<TransactionUiModel>,
        val totalAmount: String
    ) : HistoryScreenState
}

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val getExpensesByPeriodUseCase: GetExpensesByPeriodUseCase,
    private val getIncomesByPeriodUseCase: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToTransactionUiMapper
) : ViewModel() {

    private val _historyTransactionsType = MutableStateFlow(false)

    private val _historyStartDate = MutableStateFlow(getStartOfCurrentMonth())
    val historyStartDate: StateFlow<String> = _historyStartDate.asStateFlow()
    private val _historyEndDate = MutableStateFlow(getCurrentDateIso())
    val historyEndDate: StateFlow<String> = _historyEndDate.asStateFlow()

    private val _screenState = MutableStateFlow<HistoryScreenState>(HistoryScreenState.Loading)
    val screenState: StateFlow<HistoryScreenState> = _screenState.asStateFlow()

    private val _editingDateType = mutableStateOf<DateType?>(null)

    private val _showDatePickerModal = MutableStateFlow(false)
    val showDatePickerModal: StateFlow<Boolean> = _showDatePickerModal.asStateFlow()

    init {
        loadHistory()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHistory() {
        _screenState.value = HistoryScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (_historyTransactionsType.value) {
                true -> handleTransactionsResult(
                    getIncomesByPeriodUseCase(
                        accountId = 1,
                        startDate = formatHumanDateToIso(_historyStartDate.value),
                        endDate = formatHumanDateToIso(_historyEndDate.value)
                    )
                )

                false -> handleTransactionsResult(
                    getExpensesByPeriodUseCase(
                        accountId = 1,
                        startDate = formatHumanDateToIso(_historyStartDate.value),
                        endDate = formatHumanDateToIso(_historyEndDate.value)
                    )
                )
            }
        }
    }

    private fun handleTransactionsResult(result: Result<List<TransactionDomain>>) {
        result
            .onSuccess { data ->
                handleSuccess(
                    data = data.map { mapper.map(it) },
                    totalAmount = mapper.calculateTotalAmount(data)
                )
            }
            .onFailure { error -> handleError(error) }
    }

    private fun handleSuccess(
        data: List<TransactionUiModel>,
        totalAmount: String
    ) {
        _screenState.value = HistoryScreenState.Success(
            transactions = data,
            totalAmount = totalAmount
        )
    }

    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = HistoryScreenState.Error(
            messageResId = messageResId,
            retryAction = { loadHistory() }
        )
    }

    fun setHistoryTransactionsType(isIncome: Boolean) {
        _historyTransactionsType.value = isIncome
    }

    fun showDatePickerModal(type: DateType) {
        _showDatePickerModal.value = true
        _editingDateType.value = type
    }

    fun confirmDateSelection(date: Long) {
        when (_editingDateType.value as DateType) {
            DateType.START -> _historyStartDate.value = formatLongToHumanDate(date)
            DateType.END -> _historyEndDate.value = formatLongToHumanDate(date)
        }

        loadHistory()
    }

    fun onDismissDatePicker() {
        _showDatePickerModal.value = false
    }
}

enum class DateType { START, END }
