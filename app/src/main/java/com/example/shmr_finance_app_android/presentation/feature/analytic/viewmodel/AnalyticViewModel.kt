package com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.BuildConfig
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.network.AppError
import com.example.shmr_finance_app_android.core.utils.formatLongToMonthYear
import com.example.shmr_finance_app_android.core.utils.formatWithSpaces
import com.example.shmr_finance_app_android.core.utils.getCurrentMonthYear
import com.example.shmr_finance_app_android.core.utils.getFirstDayOfMonthIso
import com.example.shmr_finance_app_android.core.utils.getLastDayOfMonthIso
import com.example.shmr_finance_app_android.core.utils.getNextMonthYear
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesByPeriodUseCase
import com.example.shmr_finance_app_android.presentation.feature.analytic.model.AnalyticResultModel
import com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel.AnalyticUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel.AnalyticUiState.Empty
import com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel.AnalyticUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.analytic.viewmodel.AnalyticUiState.Loading
import com.example.shmr_finance_app_android.presentation.feature.categories.mapper.CategoryToCategoryUiMapper
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesUiState.SearchEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI‑state экрана статьи.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * 4. [Empty] Состояние при получении пустого списка категорий
 * 3. [SearchEmpty] Состояние при получении пустого списка категорий при поиске
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */

sealed interface AnalyticUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : AnalyticUiState

    /**
     * Контентный стейт, когда все данные загружены
     */
    data class Content(
        val analyticItems: List<AnalyticResultModel> = emptyList(),
        val totalAmount: String
    ) : AnalyticUiState

    /** Экран при получении пустых данных. */
    data object Empty : AnalyticUiState

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : AnalyticUiState
}

/**
 * ViewModel для экрана Статьи, реализующая:
 * 1. Загрузку данных через [getCategories]
 * 2. Преобразование доменной модели в UI-модель через [CategoryToCategoryUiMapper]
 * 3. Управление состояниями экрана ([AnalyticUiState])
 **/
class AnalyticViewModel @Inject constructor(
    private val getIncomesByPeriod: GetIncomesByPeriodUseCase,
    private val getExpensesByPeriod: GetExpensesByPeriodUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<AnalyticUiState>(Loading)
    val uiState: StateFlow<AnalyticUiState> = _uiState.asStateFlow()

    private val accountId = MutableStateFlow(BuildConfig.ACCOUNT_ID)

    private val _startMonth = MutableStateFlow(getCurrentMonthYear())
    val startMonth: StateFlow<String> = _startMonth.asStateFlow()

    private val _endMonth = MutableStateFlow(getNextMonthYear())
    val endMonth: StateFlow<String> = _endMonth.asStateFlow()

    private val _visibleModal = MutableStateFlow<AnalyticModal?>(null)
    val visibleModal: StateFlow<AnalyticModal?> = _visibleModal.asStateFlow()

    private val analyticTransactionsType = MutableStateFlow(false)

    /**
     * Инициализирует ViewModel, загружая аккаунт и категории по типу транзакции (доход/расход).
     * Устанавливает состояние загрузки, затем запрашивает данные и обновляет UI.
     * @param isIncome true, если транзакция — доход, false — расход
     */
    fun init(isIncome: Boolean) {
        analyticTransactionsType.value = isIncome
        loadData()
    }

    fun loadData() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val transactionsDef = async {
            if (analyticTransactionsType.value) {
                getIncomesByPeriod(
                    accountId = accountId.value,
                    startDate = getFirstDayOfMonthIso(startMonth.value),
                    endDate = getLastDayOfMonthIso(endMonth.value)
                )
            } else {
                getExpensesByPeriod(
                    accountId = accountId.value,
                    startDate = getFirstDayOfMonthIso(startMonth.value),
                    endDate = getLastDayOfMonthIso(endMonth.value)
                )
            }
        }

        val transactions = transactionsDef.await().getOrElse { return@launch showError(it) }
        handleResult(transactions)
    }

    private fun handleResult(transactions: List<TransactionResponseDomain>) {
        viewModelScope.launch(Dispatchers.IO) {
            if (transactions.isEmpty()) {
                _uiState.value = Empty
                return@launch
            }

            val totalAmount = transactions.sumOf { it.amount.toDouble() }
            if (totalAmount == 0.0) {
                _uiState.value = Empty
                return@launch
            }

            val amountByCategory = transactions
                .groupBy { it.category }
                .mapValues { (_, transactions) ->
                    transactions.sumOf { it.amount.toDouble() }
                }

            val currencySymbol = transactions.first().account.getCurrencySymbol()

            val analyticItems = amountByCategory.map { (category, amount) ->
                val rawPercentage = (amount / totalAmount) * 100
                var formattedPercentage = "%.1f".format(rawPercentage)

                if (formattedPercentage == "0,0") formattedPercentage = "0,1"

                AnalyticResultModel(
                    categoryId = category.id,
                    category = category.name,
                    amount = amount.toString(),
                    percentage = formattedPercentage,
                    currency = currencySymbol,
                    emoji = category.emoji
                )
            }.sortedByDescending { it.percentage }

            _uiState.value = Content(
                analyticItems = analyticItems,
                totalAmount = "${totalAmount.toString().formatWithSpaces()} $currencySymbol"
            )
        }
    }

    fun onMonthSelected(modal: AnalyticModal, timeStamp: Long) {
        val formattedDate = formatLongToMonthYear(timeStamp)
        if (modal is AnalyticModal.StartDatePicker) {
            _startMonth.value = formattedDate
        } else {
            _endMonth.value = formattedDate
        }

        loadData()
    }

    fun closeModal() {
        _visibleModal.value = null
    }

    fun openModal(modal: AnalyticModal) {
        _visibleModal.value = modal
    }

    /** Обработчик для показа ошибки */
    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}

/** Какое модальное окно сейчас открыто (если открыто). */
sealed interface AnalyticModal {
    data object StartDatePicker : AnalyticModal
    data object EndDatePicker : AnalyticModal
}

/** Содержимое snackbar‑сообщения. */
sealed interface AnalyticSnackbar {
    data class Error(@StringRes val messageResId: Int) : AnalyticSnackbar
}