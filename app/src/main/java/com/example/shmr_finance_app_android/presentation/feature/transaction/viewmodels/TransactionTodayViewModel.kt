package com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.BuildConfig
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.getCurrentDate
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.history.mapper.TransactionToTransactionUiMapper
import com.example.shmr_finance_app_android.presentation.feature.transaction.mappers.TransactionToTransactionTodayMapper
import com.example.shmr_finance_app_android.presentation.feature.transaction.models.TransactionTodayModel
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionsTodayUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionsTodayUiState.Empty
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionsTodayUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionsTodayUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI‑state экрана расходы/доходы сегодня.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * 4. [Empty] Состояние при получении пустого списка транзакций
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */
sealed interface TransactionsTodayUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : TransactionsTodayUiState

    /**
     * Контентный стейт, когда все данные загружены
     * и пользователь может взаимодействовать с формой.
     */
    data class Content(
        val transactions: List<TransactionTodayModel>,
        val totalAmount: String
    ) : TransactionsTodayUiState

    /** Экран при получении пустых данных. */
    data object Empty : TransactionsTodayUiState

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : TransactionsTodayUiState
}

/**
 * ViewModel для экрана расходы/доходы сегодня, реализующая:
 * 1. Загрузку данных расходов через [GetExpensesByPeriodUseCase]
 * 1. Загрузку данных доходов через [GetIncomesByPeriodUseCase]
 * 2. Преобразование доменной модели в UI-модель через [TransactionToTransactionUiMapper]
 * 3. Управление состояниями экрана ([TransactionsTodayUiState])
 **/
class TransactionTodayViewModel @Inject constructor(
    private val getExpensesByPeriod: GetExpensesByPeriodUseCase,
    private val getIncomesByPeriod: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToTransactionTodayMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransactionsTodayUiState>(Loading)
    val uiState: StateFlow<TransactionsTodayUiState> = _uiState.asStateFlow()

    private val accountId = MutableStateFlow(BuildConfig.ACCOUNT_ID)

    /**
     * Загружает данные о расходах, управляя состояниями:
     * 1. [Loading] - перед запросом
     * 2. [Success] или [Error] - после получения результата
     * 3. [Empty] - при получении пустого списка
     */
    fun init(isIncome: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val result = if (isIncome) {
            getIncomesByPeriod(
                accountId = accountId.value,
                startDate = getCurrentDate(),
                endDate = getCurrentDate()
            )
        } else {
            getExpensesByPeriod(
                accountId = accountId.value,
                startDate = getCurrentDate(),
                endDate = getCurrentDate()
            )
        }

        result
            .onSuccess { data ->
                if (data.isEmpty()) {
                    _uiState.value = Empty
                } else {
                    _uiState.value = Content(
                        transactions = data.sortedByDescending { it.transactionTime }
                            .map { mapper.map(it) },
                        totalAmount = mapper.calculateTotalAmount(data)
                    )
                }
            }
            .onFailure { error -> showError(error) }
    }

    /** Обработчик для показа ошибки */
    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}