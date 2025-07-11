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
 * UI‑state экрана создания транзакции.
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
        val form: TransactionCreationForm = TransactionCreationForm(),
        val categories: List<CategoryUiModel> = emptyList(),
        val visibleModal: TransactionCreationModal? = null,
        val snackbar: TransactionCreationSnackbar? = null
    ) : TransactionCreationUiState {
        /** Валиден ли ввод и активна ли кнопка Сохранить. */
        val isSaveEnabled: Boolean get() = form.amount.toIntOrNull() != null
    }

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : TransactionCreationUiState
}

/** Эвенты Snackbar + Navigate */
sealed interface TransactionCreationEvent {
    data class ShowSnackBar(@StringRes val messageResId: Int) : TransactionCreationEvent
    data object NavigateBack : TransactionCreationEvent
}

class TransactionCreationViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val accountMapper: AccountToBalanceBriefMapper,
    private val getCategoriesByType: GetCategoriesByTypeUseCase,
    private val categoryMapper: CategoryToCategoryUiMapper,
    private val createTransaction: CreateTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TransactionCreationUiState>(Loading)
    val uiState: StateFlow<TransactionCreationUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TransactionCreationEvent>()
    val events: SharedFlow<TransactionCreationEvent> = _events

    private fun contentOrNull() = _uiState.value as? Content

    /**
     * Обновляет состояние UI, применяя функцию преобразования к текущему состоянию типа [Content].
     * @param transform функция, преобразующая текущее [Content] в новое состояние
     */
    private fun updateContent(transform: (Content) -> Content) {
        _uiState.update { ui ->
            if (ui is Content) transform(ui) else ui
        }
    }

    /**
     * Инициализирует ViewModel, загружая аккаунт и категории по типу транзакции (доход/расход).
     * Устанавливает состояние загрузки, затем запрашивает данные и обновляет UI.
     * @param isIncome true, если транзакция — доход, false — расход
     */
    fun init(isIncome: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val accountDef = async { getAccount(Constants.TEST_ACCOUNT_ID) }
        val categoriesDef = async { getCategoriesByType(isIncome) }

        val account = accountDef.await().getOrElse { return@launch showError(it) }
        val categories = categoriesDef.await().getOrElse { return@launch showError(it) }

        _uiState.value = Content(
            form = TransactionCreationForm(
                balance = accountMapper.map(account).name,
                currencySymbol = accountMapper.map(account).currencySymbol,
                selectedCategory = categoryMapper.map(categories.first()),
                isIncome = isIncome
            ),
            categories = categories.map { categoryMapper.map(it) }
        )
    }

    /**
     * Обрабатывает изменение полей формы создания транзакции.
     * @param field изменённое поле формы
     * @param value новое значение поля
     */
    fun onFieldChanged(field: TransactionCreationField, value: Any) {
        updateContent { content ->
            val f = content.form
            val newForm = when (field) {
                TransactionCreationField.BALANCE -> f.copy(balance = value as String)
                TransactionCreationField.CATEGORY -> f.copy(selectedCategory = value as CategoryUiModel)
                TransactionCreationField.AMOUNT -> f.copy(amount = value as String)
                TransactionCreationField.COMMENT -> f.copy(comment = value as String)
                TransactionCreationField.DATE -> f.copy(date = formatLongToHumanDate(value as Long))
                TransactionCreationField.TIME -> f.copy(time = (value as Pair<Int, Int>).toTimeString())
            }
            content.copy(form = newForm)
        }
    }

    fun closeModal() = _uiState.update { (it as Content).copy(visibleModal = null) }
    fun openModal(modal: TransactionCreationModal) = _uiState.update {
        (it as Content).copy(visibleModal = modal)
    }

    /**
     * Показывает Snackbar с сообщением из ресурсов.
     * @param resId ресурсный идентификатор строки сообщения
     */
    private fun showSnackbar(@StringRes resId: Int) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _events.emit(TransactionCreationEvent.ShowSnackBar(resId))
        }
    }

    /**
     * Запускает процесс создания транзакции.
     * Проверяет валидность формы, если невалидна — показывает ошибку,
     * иначе вызывает use case для создания транзакции и обрабатывает результат.
     */
    fun createTransaction() = viewModelScope.launch(Dispatchers.IO) {
        val content = contentOrNull() ?: return@launch

        val form = content.form

        if (!content.isSaveEnabled) {
            showSnackbar(R.string.amount_error_message)
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
            .onSuccess { _events.emit(TransactionCreationEvent.NavigateBack) }
            .onFailure {
                _uiState.value = content
                showSnackbar(R.string.error_message)
            }
    }

    /** Обработчик для показа ошибки */
    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}

enum class TransactionCreationField {
    BALANCE,
    CATEGORY,
    AMOUNT,
    COMMENT,
    DATE,
    TIME
}

/** Текущие значения полей формы. */
data class TransactionCreationForm(
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
sealed interface TransactionCreationModal {
    data object DatePicker : TransactionCreationModal
    data object TimePicker : TransactionCreationModal
    data object CategoryPicker : TransactionCreationModal
}

/** Содержимое snackbar‑сообщения. */
sealed interface TransactionCreationSnackbar {
    data class Error(@StringRes val messageResId: Int) : TransactionCreationSnackbar
}

private fun Pair<Int, Int>.toTimeString(): String = "%02d:%02d".format(first, second)