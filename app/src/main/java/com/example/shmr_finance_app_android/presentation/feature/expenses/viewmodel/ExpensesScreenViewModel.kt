package com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.getCurrentDate
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenState
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
    data object Loading : ExpensesScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : ExpensesScreenState
    data object Empty : ExpensesScreenState
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
            val expensesTransactions = getTransactionsByPeriod(
                accountId = 1,
                startDate = getCurrentDate(),
                endDate = getCurrentDate()
            )

            expensesTransactions.onSuccess { data ->
                if (data.isEmpty()) {
                    _screenState.value = ExpensesScreenState.Empty
                } else {
                    _screenState.value = ExpensesScreenState.Success(
                        expenses = data.map { mapper.map(it) },
                        totalAmount = mapper.calculateTotalAmount(data)
                    )
                }
            }.onFailure { error ->
                val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
                _screenState.value = ExpensesScreenState.Error(
                    messageResId = messageResId,
                    retryAction = { loadExpenses() }
                )
            }
        }
    }
}
