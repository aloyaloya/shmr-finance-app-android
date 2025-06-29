package com.example.shmr_finance_app_android.presentation.feature.history.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.Constants
import com.example.shmr_finance_app_android.core.utils.formatHumanDateToIso
import com.example.shmr_finance_app_android.core.utils.formatLongToHumanDate
import com.example.shmr_finance_app_android.core.utils.getCurrentDateIso
import com.example.shmr_finance_app_android.core.utils.getStartOfCurrentMonth
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.history.mapper.TransactionToTransactionUiMapper
import com.example.shmr_finance_app_android.presentation.feature.history.model.TransactionUiModel
import com.example.shmr_finance_app_android.presentation.feature.history.viewmodel.HistoryScreenState.Error
import com.example.shmr_finance_app_android.presentation.feature.history.viewmodel.HistoryScreenState.Loading
import com.example.shmr_finance_app_android.presentation.feature.history.viewmodel.HistoryScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Состояния экрана История с явным разделением:
 * - [Loading] Начальное состояние загрузки
 * - [Error] Состояние ошибки с:
 * - Локализованным сообщением ([messageResId])
 * - Коллбэком повторной попытки ([retryAction])
 * - [Success] Состояние успешной загрузки с:
 * - Списком готовых моделей ([TransactionUiModel])
 * - Общей суммой расходов/доходов ([totalAmount])
 */
sealed interface HistoryScreenState {
    data object Loading : HistoryScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : HistoryScreenState
    data object Empty : HistoryScreenState
    data class Success(
        val transactions: List<TransactionUiModel>,
        val totalAmount: String
    ) : HistoryScreenState
}

/**
 * ViewModel для экрана История, реализующая:
 * 1. Разделение UseCase для расходов/доходов
 * - Для доходов ([GetIncomesByPeriodUseCase])
 * - Для расходов ([GetExpensesByPeriodUseCase])
 * 2. Преобразование доменной модели в UI-модель через [TransactionToTransactionUiMapper]
 * 3. Управление состояниями экрана ([HistoryScreenState])
 * 4. Обработку выбора дат
 **/
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HistoryScreenViewModel @Inject constructor(
    private val getExpensesByPeriodUseCase: GetExpensesByPeriodUseCase,
    private val getIncomesByPeriodUseCase: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToTransactionUiMapper
) : ViewModel() {

    /** Тип отображаемых транзакций (false = расходы, true = доходы) */
    private val _historyTransactionsType = MutableStateFlow(false)

    /** Начальная дата периода (изначально начало текущего месяца) */
    private val _historyStartDate = MutableStateFlow(getStartOfCurrentMonth())
    val historyStartDate: StateFlow<String> = _historyStartDate.asStateFlow()

    /** Конечная дата периода (изначально текущая дата) */
    private val _historyEndDate = MutableStateFlow(getCurrentDateIso())
    val historyEndDate: StateFlow<String> = _historyEndDate.asStateFlow()

    private val _screenState = MutableStateFlow<HistoryScreenState>(Loading)
    val screenState: StateFlow<HistoryScreenState> = _screenState.asStateFlow()

    /** Текущий редактируемый тип даты (для DatePicker диалога) */
    private val _editingDateType = mutableStateOf<DateType?>(null)

    /** Флаг видимости диалога выбора даты */
    private val _showDatePickerModal = MutableStateFlow(false)
    val showDatePickerModal: StateFlow<Boolean> = _showDatePickerModal.asStateFlow()

    init {
        loadHistory()
    }

    /**
     * Загружает историю операций с текущими параметрами:
     * 1. Устанавливает состояние Loading
     * 2. Выбирает соответствующий UseCase
     * 3. Обрабатывает результат
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun loadHistory() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (_historyTransactionsType.value) {
                true -> handleTransactionsResult(
                    getIncomesByPeriodUseCase(
                        accountId = Constants.TEST_ACCOUNT_ID,
                        startDate = formatHumanDateToIso(_historyStartDate.value),
                        endDate = formatHumanDateToIso(_historyEndDate.value)
                    )
                )

                false -> handleTransactionsResult(
                    getExpensesByPeriodUseCase(
                        accountId = Constants.TEST_ACCOUNT_ID,
                        startDate = formatHumanDateToIso(_historyStartDate.value),
                        endDate = formatHumanDateToIso(_historyEndDate.value)
                    )
                )
            }
        }
    }

    /**
     * Обрабатывает результат запроса, преобразуя:
     * - Успех -> [TransactionUiModel] через маппер
     * - Ошибку -> Сообщение об ошибке
     */
    private fun handleTransactionsResult(result: Result<List<TransactionDomain>>) {
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
        data: List<TransactionUiModel>,
        totalAmount: String
    ) {
        _screenState.value = Success(
            transactions = data,
            totalAmount = totalAmount
        )
    }

    /** Обрабатывает ошибку */
    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadHistory() }
        )
    }

    /**
     * Переключает тип отображаемых транзакций
     * @param isIncome true для доходов, false для расходов
     */
    fun setHistoryTransactionsType(isIncome: Boolean) {
        _historyTransactionsType.value = isIncome
    }

    /**
     * Показывает диалог выбора даты
     * @param type Тип даты (начальная/конечная)
     */
    fun showDatePickerModal(type: DateType) {
        _showDatePickerModal.value = true
        _editingDateType.value = type
    }

    /**
     * Подтверждает выбор даты и перезагружает данные
     * @param date Выбранная дата в timestamp
     */
    fun confirmDateSelection(date: Long) {
        when (_editingDateType.value as DateType) {
            DateType.START -> _historyStartDate.value = formatLongToHumanDate(date)
            DateType.END -> _historyEndDate.value = formatLongToHumanDate(date)
        }
        _showDatePickerModal.value = false
        loadHistory()
    }

    /** Скрывает диалог выбора даты */
    fun onDismissDatePicker() {
        _showDatePickerModal.value = false
    }
}

enum class DateType { START, END }
