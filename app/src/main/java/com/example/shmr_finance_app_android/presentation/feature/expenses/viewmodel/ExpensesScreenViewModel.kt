package com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.getCurrentDate
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.balance.model.BalanceUiModel
import com.example.shmr_finance_app_android.presentation.feature.expenses.mapper.TransactionToExpenseMapper
import com.example.shmr_finance_app_android.presentation.feature.expenses.model.ExpenseUiModel
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesScreenState.Error
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesScreenState.Loading
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Состояния экрана Расходы с явным разделением:
 * - [Loading] Начальное состояние загрузки
 * - [Error] Состояние ошибки с:
 * - Локализованным сообщением ([messageResId])
 * - Коллбэком повторной попытки ([retryAction])
 * - [Success] Состояние успешной загрузки с:
 * - Списком готовых моделей ([BalanceUiModel])
 * - Общей суммой расходов ([totalAmount])
 */
sealed interface ExpensesScreenState {
    data object Loading : ExpensesScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : ExpensesScreenState
    data object Empty : ExpensesScreenState
    data class Success(
        val expenses: List<ExpenseUiModel>,
        val totalAmount: String
    ) : ExpensesScreenState
}

/**
 * ViewModel для экрана Расходы, реализующая:
 * 1. Загрузку данных через [GetExpensesByPeriodUseCase]
 * 2. Преобразование доменной модели в UI-модель через [TransactionToExpenseMapper]
 * 3. Управление состояниями экрана ([ExpensesScreenState])
 **/
@HiltViewModel
class ExpensesScreenViewModel @Inject constructor(
    private val getTransactionsByPeriod: GetExpensesByPeriodUseCase,
    private val mapper: TransactionToExpenseMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<ExpensesScreenState>(Loading)
    val screenState: StateFlow<ExpensesScreenState> = _screenState.asStateFlow()

    init {
        loadExpenses()
    }

    /**
     * Загружает данные о расходах, управляя состояниями:
     * 1. [Loading] - перед запросом
     * 2. [Success] или [Error] - после получения результата
     */
    private fun loadExpenses() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleExpensesResult(
                getTransactionsByPeriod(
                    accountId = 1,
                    startDate = getCurrentDate(),
                    endDate = getCurrentDate()
                )
            )
        }
    }

    /**
     * Обрабатывает результат запроса, преобразуя:
     * - Успех -> [ExpenseUiModel] через маппер
     * - Ошибку -> Сообщение об ошибке
     */
    private fun handleExpensesResult(result: Result<List<TransactionDomain>>) {
        result
            .onSuccess { data ->
                handleSuccess(
                    data = data.map { mapper.map(it) },
                    totalAmount = mapper.calculateTotalAmount(data)
                )
            }
            .onFailure { error -> handleError(error) }
    }

    /** Обновляет состояние при успешной загрузке */
    private fun handleSuccess(
        data: List<ExpenseUiModel>,
        totalAmount: String
    ) {
        _screenState.value = Success(
            expenses = data,
            totalAmount = totalAmount
        )
    }

    /** Обрабатывает ошибку */
    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadExpenses() }
        )
    }
}
