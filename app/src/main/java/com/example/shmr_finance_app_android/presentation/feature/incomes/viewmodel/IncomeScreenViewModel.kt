package com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.Constants
import com.example.shmr_finance_app_android.core.utils.getCurrentDate
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.incomes.mapper.TransactionToIncomeMapper
import com.example.shmr_finance_app_android.presentation.feature.incomes.model.IncomeUiModel
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomesUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomesUiState.Empty
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomesUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel.IncomesUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI‑state экрана доходы.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * 4. [Empty] Состояние при получении пустого списка расходов
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */
sealed interface IncomesUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : IncomesUiState

    /**
     * Контентный стейт, когда все данные загружены
     * и пользователь может взаимодействовать с формой.
     */
    data class Content(
        val incomes: List<IncomeUiModel>,
        val totalAmount: String
    ) : IncomesUiState

    /** Экран при получении пустых данных. */
    data object Empty : IncomesUiState

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : IncomesUiState
}

/**
 * ViewModel для экрана Доходы, реализующая:
 * 1. Загрузку данных через [GetIncomesByPeriodUseCase]
 * 2. Преобразование доменной модели в UI-модель через [TransactionToIncomeMapper]
 * 3. Управление состояниями экрана ([IncomesUiState])
 **/
class IncomeScreenViewModel @Inject constructor(
    private val getTransactionsByPeriod: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToIncomeMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<IncomesUiState>(Loading)
    val uiState: StateFlow<IncomesUiState> = _uiState.asStateFlow()

    /**
     * Загружает данные о дохода, управляя состояниями:
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
                    incomes = data.sortedByDescending { it.transactionTime }
                        .map { mapper.map(it) },
                    totalAmount = mapper.calculateTotalAmount(data)
                )
            }
            .onFailure { error -> showError(error) }
    }

    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}