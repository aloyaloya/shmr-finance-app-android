package com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.core.utils.getCurrentDate
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.shmr_finance_app_android.presentation.feature.expenses.mapper.TransactionToExpenseMapper
import com.example.shmr_finance_app_android.presentation.feature.expenses.model.ExpenseUiModel
import javax.inject.Inject

sealed interface ExpensesScreenState {
    object Loading : ExpensesScreenState
    data class Error(val message: String, val retryAction: () -> Unit) : ExpensesScreenState
    object Empty : ExpensesScreenState
    data class Success(
        val expenses: List<ExpenseUiModel>,
        val totalAmount: String
    ) : ExpensesScreenState
}

@HiltViewModel
class ExpensesScreenViewModel @Inject constructor(
    private val getTransactionsByPeriod: GetExpensesByPeriodUseCase,
    private val mapper: TransactionToExpenseMapper
) : ViewModel() {
    private val _screenState = MutableStateFlow<ExpensesScreenState>(ExpensesScreenState.Loading)
    val screenState: StateFlow<ExpensesScreenState> = _screenState.asStateFlow()

    init {
        loadExpenses()
    }

    private fun loadExpenses() {
        _screenState.value = ExpensesScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val expensesTransactions = getTransactionsByPeriod(
                    accountId = 1,
                )
                if (expensesTransactions.isEmpty()) {
                    _screenState.value = ExpensesScreenState.Empty
                } else {
                    _screenState.value = ExpensesScreenState.Success(
                        expenses = expensesTransactions.map { mapper.map(it) },
                        totalAmount = mapper.calculateTotalAmount(expensesTransactions)
                    )
                }
            } catch (e: Exception) {
                _screenState.value = ExpensesScreenState.Error(
                    message = e.message ?: "Не удалось загрузить ваши расходы",
                    retryAction = { loadExpenses() }
                )
            }
        }
    }
}
