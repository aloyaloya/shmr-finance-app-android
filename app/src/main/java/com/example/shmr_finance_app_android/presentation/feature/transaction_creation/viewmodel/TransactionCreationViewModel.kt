package com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.Constants
import com.example.shmr_finance_app_android.core.utils.formatHumanDateToIso
import com.example.shmr_finance_app_android.core.utils.formatLongToHumanDate
import com.example.shmr_finance_app_android.core.utils.getCurrentDateIso
import com.example.shmr_finance_app_android.core.utils.getCurrentTime
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.CreateTransactionUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetAccountUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetCategoriesByTypeUseCase
import com.example.shmr_finance_app_android.presentation.feature.categories.mapper.CategoryToCategoryUiMapper
import com.example.shmr_finance_app_android.presentation.feature.categories.model.CategoryUiModel
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.mapper.AccountToBalanceBriefMapper
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionCreationUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionCreationUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.viewmodel.TransactionCreationUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI‑state экрана создания/редактирования транзакции.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */
sealed interface TransactionCreationUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : TransactionCreationUiState

    /**
     * Контентный стейт, когда все данные загружены
     * и пользователь может взаимодействовать с формой.
     */
    data class Content(
        val form: TransactionForm = TransactionForm(),
        val categories: List<CategoryUiModel> = emptyList(),
        val visibleModal: Modal? = null,
        val snackbar: Snackbar? = null
    ) : TransactionCreationUiState {
        /** Валиден ли ввод и активна ли кнопка Сохранить. */
        val isSaveEnabled: Boolean get() = form.amount.toIntOrNull() != null
    }

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : TransactionCreationUiState
}

/** Эвенты Snackbar + Navigate */
sealed interface TransactionEvent {
    data class ShowSnackBar(@StringRes val messageResId: Int) : TransactionEvent
    data object NavigateBack : TransactionEvent
}

class TransactionCreationViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val accountMapper: AccountToBalanceBriefMapper,
    private val getCategoriesByType: GetCategoriesByTypeUseCase,
    private val categoryMapper: CategoryToCategoryUiMapper,
    private val createTransaction: CreateTransactionUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<TransactionCreationUiState>(Loading)
    val uiState: StateFlow<TransactionCreationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TransactionEvent>()
    val events: SharedFlow<TransactionEvent> = _events

    private fun contentOrNull() = _uiState.value as? Content

    private fun updateContent(transform: (Content) -> Content) {
        _uiState.update { ui ->
            if (ui is Content) transform(ui) else ui
        }
    }

    fun init(isIncome: Boolean) = viewModelScope.launch {
        _uiState.value = Loading

        val accountDef = async { getAccount(Constants.TEST_ACCOUNT_ID) }
        val categoriesDef = async { getCategoriesByType(isIncome) }

        val account = accountDef.await().getOrElse { return@launch showError(it) }
        val categories = categoriesDef.await().getOrElse { return@launch showError(it) }

        _uiState.value = Content(
            form = TransactionForm(
                balance = accountMapper.map(account).name,
                currencySymbol = accountMapper.map(account).currencySymbol,
                selectedCategory = categoryMapper.map(categories.first()),
                isIncome = isIncome
            ),
            categories = categories.map { categoryMapper.map(it) }
        )
    }

    fun onFieldChanged(field: TransactionField, value: Any) {
        updateContent { content ->
            val f = content.form
            val newForm = when (field) {
                TransactionField.BALANCE -> f.copy(balance = value as String)
                TransactionField.CATEGORY -> f.copy(selectedCategory = value as CategoryUiModel)
                TransactionField.AMOUNT -> f.copy(amount = value as String)
                TransactionField.COMMENT -> f.copy(comment = value as String)
                TransactionField.DATE -> f.copy(date = formatLongToHumanDate(value as Long))
                TransactionField.TIME -> f.copy(time = (value as Pair<Int, Int>).toTimeString())
            }
            content.copy(form = newForm)
        }
    }

    fun openModal(modal: Modal) = _uiState.update { (it as Content).copy(visibleModal = modal) }
    fun closeModal() = _uiState.update { (it as Content).copy(visibleModal = null) }

    private fun showSnackbar(@StringRes resId: Int) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _events.emit(TransactionEvent.ShowSnackBar(resId))
        }
    }

    fun createTransaction() = viewModelScope.launch(Dispatchers.IO) {
        val content = contentOrNull() ?: return@launch

        val form = content.form

        if (!content.isSaveEnabled) {
            showSnackbar(R.string.balance_amount_error_message)
            return@launch
        }

        _uiState.value = Loading

        val result = createTransaction(
            accountId = Constants.TEST_ACCOUNT_ID,
            categoryId = form.selectedCategory?.id ?: 0,
            amount = form.amount,
            transactionDate = formatHumanDateToIso(form.date),
            transactionTime = form.time,
            comment = form.comment
        )

        result
            .onSuccess { _events.emit(TransactionEvent.NavigateBack) }
            .onFailure {
                _uiState.value = content
                showSnackbar(R.string.error_message)
            }
    }

    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}

enum class TransactionField {
    BALANCE,
    CATEGORY,
    AMOUNT,
    COMMENT,
    DATE,
    TIME
}

/** Текущие значения полей формы. */
data class TransactionForm(
    val balance: String = "",
    val currencySymbol: String = "$",
    val selectedCategory: CategoryUiModel? = null,
    val date: String = getCurrentDateIso(),
    val time: String = getCurrentTime(),
    val amount: String = "",
    val comment: String = "",
    val isIncome: Boolean = true
)

/** Какое модальное окно сейчас открыто (если открыто). */
sealed interface Modal {
    data object DatePicker : Modal
    data object TimePicker : Modal
    data object CategoryPicker : Modal
}

/** Содержимое snackbar‑сообщения. */
sealed interface Snackbar {
    data class Error(@StringRes val messageResId: Int) : Snackbar
}

private fun Pair<Int, Int>.toTimeString(): String = "%02d:%02d".format(first, second)