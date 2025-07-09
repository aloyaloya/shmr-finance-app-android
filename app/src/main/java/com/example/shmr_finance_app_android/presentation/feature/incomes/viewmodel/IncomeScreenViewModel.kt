package com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.Constants
import com.example.shmr_finance_app_android.core.utils.getCurrentDate
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.incomes.mapper.TransactionToIncomeMapper
import com.example.shmr_finance_app_android.presentation.feature.incomes.model.IncomeUiModel
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomeScreenState.Error
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomeScreenState.Loading
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomeScreenState.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Состояния экрана Доходы с явным разделением:
 * - [Loading] Начальное состояние загрузки
 * - [Error] Состояние ошибки с:
 * - Локализованным сообщением ([messageResId])
 * - Коллбэком повторной попытки ([retryAction])
 * - [Success] Состояние успешной загрузки с:
 * - Списком готовых моделей ([IncomeUiModel])
 * - Общей суммой расходов ([totalAmount])
 */
sealed interface IncomeScreenState {
    data object Loading : IncomeScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : IncomeScreenState
    data object Empty : IncomeScreenState
    data class Success(
        val incomes: List<IncomeUiModel>,
        val totalAmount: String
    ) : IncomeScreenState
}

/**
 * ViewModel для экрана Доходы, реализующая:
 * 1. Загрузку данных через [GetIncomesByPeriodUseCase]
 * 2. Преобразование доменной модели в UI-модель через [TransactionToIncomeMapper]
 * 3. Управление состояниями экрана ([IncomeScreenState])
 **/
class IncomeScreenViewModel @Inject constructor(
    private val getTransactionsByPeriodUseCase: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToIncomeMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<IncomeScreenState>(Loading)
    val screenState: StateFlow<IncomeScreenState> = _screenState.asStateFlow()

    fun initialize() {
        loadIncomes()
    }

    /**
     * Загружает данные о дохода, управляя состояниями:
     * 1. [Loading] - перед запросом
     * 2. [Success] или [Error] - после получения результата
     */
    private fun loadIncomes() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleIncomesResult(
                getTransactionsByPeriodUseCase(
                    accountId = Constants.TEST_ACCOUNT_ID,
                    startDate = getCurrentDate(),
                    endDate = getCurrentDate()
                )
            )
        }
    }

    /**
     * Обрабатывает результат запроса, преобразуя:
     * - Успех -> [IncomeUiModel] через маппер
     * - Ошибку -> Сообщение об ошибке
     */
    private fun handleIncomesResult(result: Result<List<TransactionDomain>>) {
        result
            .onSuccess { data ->
                handleSuccess(
                    data = data.sortedByDescending { it.transactionTime }.map { mapper.map(it) },
                    totalAmount = mapper.calculateTotalAmount(data)
                )
            }
            .onFailure { error -> handleError(error) }
    }

    /** Обновляет состояние при успешной загрузке */
    private fun handleSuccess(
        data: List<IncomeUiModel>,
        totalAmount: String
    ) {
        _screenState.value = if (data.isEmpty()) {
            IncomeScreenState.Empty
        } else {
            Success(
                incomes = data,
                totalAmount = totalAmount
            )
        }
    }

    /** Обрабатывает ошибку */
    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadIncomes() }
        )
    }
}