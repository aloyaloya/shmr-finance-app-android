package com.example.shmr_finance_app_android.presentation.feature.history.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.core.utils.formatDateToRussian
import com.example.shmr_finance_app_android.core.utils.getStartAndEndOfCurrentMonth
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
    data class Error(val message: String, val retryAction: () -> Unit) : HistoryScreenState
    data object Empty : HistoryScreenState
    data class Success(
        val transactions: List<TransactionUiModel>,
        val totalAmount: String,
        val startDate: String,
        val endDate: String
    ) : HistoryScreenState
}

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val getExpensesByPeriodUseCase: GetExpensesByPeriodUseCase,
    private val getIncomesByPeriodUseCase: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToTransactionUiMapper
) : ViewModel() {
    private val _historyTransactionsType = MutableStateFlow(false)
    private val _historyDates = MutableStateFlow(getStartAndEndOfCurrentMonth())
    private val _screenState = MutableStateFlow<HistoryScreenState>(
        HistoryScreenState.Loading
    )
    val screenState: StateFlow<HistoryScreenState> = _screenState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _screenState.value = HistoryScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val transactions = when (_historyTransactionsType.value) {
                    true -> getIncomesByPeriodUseCase(
                        accountId = 1,
                        startDate = _historyDates.value.first,
                        endDate = _historyDates.value.second
                    )
                    false -> getExpensesByPeriodUseCase(
                        accountId = 1,
                        startDate = _historyDates.value.first,
                        endDate = _historyDates.value.second
                    )
                }

                _screenState.value = if (transactions.isEmpty()) {
                    HistoryScreenState.Empty
                } else {
                    HistoryScreenState.Success(
                        transactions = transactions.map { mapper.map(it) },
                        totalAmount = mapper.calculateTotalAmount(transactions),
                        startDate = formatDateToRussian(_historyDates.value.first),
                        endDate = formatDateToRussian(_historyDates.value.second)
                    )
                }
            } catch (e: Exception) {
                _screenState.value = HistoryScreenState.Error(
                    message = e.message ?: "Неизвестная ошибка",
                    retryAction = { loadHistory() }
                )
            }
        }
    }

    fun setHistoryTransactionsType(isIncome: Boolean) {
        _historyTransactionsType.value = isIncome
    }
}