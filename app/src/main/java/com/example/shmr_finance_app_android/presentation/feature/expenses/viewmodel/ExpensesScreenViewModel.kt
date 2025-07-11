package com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.Constants
import com.example.shmr_finance_app_android.core.utils.getCurrentDate
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.expenses.mapper.TransactionToExpenseMapper
import com.example.shmr_finance_app_android.presentation.feature.expenses.model.ExpenseUiModel
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesUiState.Empty
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.expenses.viewmodel.ExpensesUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI‑state экрана расходы.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * 4. [Empty] Состояние при получении пустого списка расходов
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */
sealed interface ExpensesUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : ExpensesUiState

    /**
     * Контентный стейт, когда все данные загружены
     * и пользователь может взаимодействовать с формой.
     */
    data class Content(
        val expenses: List<ExpenseUiModel>,
        val totalAmount: String
    ) : ExpensesUiState

    /** Экран при получении пустых данных. */
    data object Empty : ExpensesUiState

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : ExpensesUiState
}

/**
 * ViewModel для экрана Расходы, реализующая:
 * 1. Загрузку данных через [GetExpensesByPeriodUseCase]
 * 2. Преобразование доменной модели в UI-модель через [TransactionToExpenseMapper]
 * 3. Управление состояниями экрана ([ExpensesUiState])
 **/
class ExpensesScreenViewModel @Inject constructor(
    private val getTransactionsByPeriod: GetExpensesByPeriodUseCase,
    private val mapper: TransactionToExpenseMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExpensesUiState>(Loading)
    val uiState: StateFlow<ExpensesUiState> = _uiState.asStateFlow()

    /**
     * Загружает данные о расходах, управляя состояниями:
     * 1. [Loading] - перед запросом
     * 2. [Success] или [Error] - после получения результата
     */
    fun init() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val result = getTransactionsByPeriod(
            accountId = Constants.TEST_ACCOUNT_ID,
            startDate = getCurrentDate(),
            endDate = getCurrentDate()
        )

        result
            .onSuccess { data ->
                _uiState.value = Content(
                    expenses = data.sortedByDescending { it.transactionTime }
                        .map { mapper.map(it) },
                    totalAmount = mapper.calculateTotalAmount(data)
                )
            }
            .onFailure { error -> showError(error) }
    }

    /** Обработчик для показа ошибки */
    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}
