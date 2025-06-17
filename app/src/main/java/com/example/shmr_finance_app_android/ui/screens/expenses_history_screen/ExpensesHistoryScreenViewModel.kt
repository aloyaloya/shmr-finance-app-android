package com.example.shmr_finance_app_android.ui.screens.expenses_history_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.data.model.domain.Expense
import com.example.shmr_finance_app_android.data.model.mockExpenses
import com.example.shmr_finance_app_android.data.model.mockHistoryExpenses
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface ExpensesHistoryScreenState {
    object Loading : ExpensesHistoryScreenState
    data class Error(val message: String, val retryAction: () -> Unit) : ExpensesHistoryScreenState
    object Empty : ExpensesHistoryScreenState
    data class Success(
        val expenses: List<Expense>,
        val totalAmount: Int,
        val startDate: String,
        val endDate: String
    ) : ExpensesHistoryScreenState
}

class ExpensesHistoryScreenViewModel : ViewModel() {
    private val _screenState = MutableStateFlow<ExpensesHistoryScreenState>(
        ExpensesHistoryScreenState.Loading
    )
    val screenState: StateFlow<ExpensesHistoryScreenState> = _screenState.asStateFlow()

    init {
        loadHistory()
    }

    private fun loadHistory() {
        _screenState.value = ExpensesHistoryScreenState.Loading
        viewModelScope.launch {
            try {
                val expenses = mockHistoryExpenses
                val mockStartDate = "Февраль 2025"
                val mockEndDate = "Март 2025"
                _screenState.value = if (expenses.isEmpty()) {
                    ExpensesHistoryScreenState.Empty
                } else {
                    ExpensesHistoryScreenState.Success(
                        expenses = expenses,
                        totalAmount = expenses.sumOf { it.amount },
                        startDate = mockStartDate,
                        endDate = mockEndDate
                    )
                }
            } catch (e: Exception) {
                _screenState.value = ExpensesHistoryScreenState.Error(
                    message = e.message ?: "Неизвестна ошибка",
                    retryAction = { loadHistory() }
                )
            }
        }
    }
}