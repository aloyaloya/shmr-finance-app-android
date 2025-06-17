package com.example.shmr_finance_app_android.ui.screens.expenses_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.data.model.domain.Expense
import com.example.shmr_finance_app_android.data.model.mockExpenses
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface ExpensesScreenState {
    object Loading : ExpensesScreenState
    data class Error(val message: String, val retryAction: () -> Unit) : ExpensesScreenState
    object Empty : ExpensesScreenState
    data class Success(
        val expenses: List<Expense>,
        val totalAmount: Int
    ) : ExpensesScreenState
}

@HiltViewModel
class ExpensesScreenViewModel @Inject constructor() : ViewModel() {
    private val _screenState = MutableStateFlow<ExpensesScreenState>(ExpensesScreenState.Loading)
    val screenState: StateFlow<ExpensesScreenState> = _screenState.asStateFlow()

    init {
        loadPayments()
    }

    private fun loadPayments() {
        _screenState.value = ExpensesScreenState.Loading
        viewModelScope.launch {
            try {
                val payments = mockExpenses
                _screenState.value = if (payments.isEmpty()) {
                    ExpensesScreenState.Empty
                } else {
                    ExpensesScreenState.Success(
                        expenses = payments,
                        totalAmount = payments.sumOf { it.amount }
                    )
                }
            } catch (e: Exception) {
                _screenState.value = ExpensesScreenState.Error(
                    message = e.message ?: "Неизвестна ошибка",
                    retryAction = { loadPayments() }
                )
            }
        }
    }
}