package com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.BuildConfig
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.formatHumanDateToIso
import com.example.shmr_finance_app_android.core.utils.formatIsoDateToHuman
import com.example.shmr_finance_app_android.core.utils.formatLongToHumanDate
import com.example.shmr_finance_app_android.core.utils.getCurrentDateIso
import com.example.shmr_finance_app_android.core.utils.getCurrentTime
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.DeleteTransactionUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetCategoriesByTypeUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetTransactionUseCase
import com.example.shmr_finance_app_android.domain.usecases.UpdateTransactionUseCase
import com.example.shmr_finance_app_android.presentation.feature.categories.mapper.CategoryToCategoryUiMapper
import com.example.shmr_finance_app_android.presentation.feature.categories.model.CategoryUiModel
import com.example.shmr_finance_app_android.presentation.feature.transaction.mappers.TransactionResponseToTransactionDetailed
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.transaction.viewmodels.TransactionUpdateUiState.Loading
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
 * UI‑state экрана редактирования транзакции.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */
sealed interface TransactionUpdateUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : TransactionUpdateUiState

    /**
     * Контентный стейт, когда все данные загружены
     * и пользователь может взаимодействовать с формой.
     */
    data class Content(
        val form: TransactionUpdateForm = TransactionUpdateForm(),
        val categories: List<CategoryUiModel> = emptyList(),
        val visibleModal: TransactionUpdateModal? = null,
        val snackbar: TransactionUpdateSnackbar? = null
    ) : TransactionUpdateUiState {
        /** Валиден ли ввод и активна ли кнопка Сохранить. */
        val isSaveEnabled: Boolean get() = form.amount.toIntOrNull() != null
    }

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : TransactionUpdateUiState
}

/** Эвенты Snackbar + Navigate */
sealed interface TransactionUpdateEvent {
    data class ShowSnackBar(@StringRes val messageResId: Int) : TransactionUpdateEvent
    data object NavigateBack : TransactionUpdateEvent
}

class TransactionUpdateViewModel @Inject constructor(
    private val getCategoriesByType: GetCategoriesByTypeUseCase,
    private val categoryMapper: CategoryToCategoryUiMapper,
    private val getTransaction: GetTransactionUseCase,
    private val transactionMapper: TransactionResponseToTransactionDetailed,
    private val updateTransactionUseCase: UpdateTransactionUseCase,
    private val deleteTransactionUseCase: DeleteTransactionUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<TransactionUpdateUiState>(Loading)
    val uiState: StateFlow<TransactionUpdateUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TransactionUpdateEvent>()
    val events: SharedFlow<TransactionUpdateEvent> = _events

    private fun contentOrNull() = _uiState.value as? Content

    private val accountId = MutableStateFlow(BuildConfig.ACCOUNT_ID)

    /**
     * Обновляет состояние UI, если текущее состояние - [Content].
     * @param transform лямбда-функция для обновления контента
     */
    private fun updateContent(transform: (Content) -> Content) {
        _uiState.update { ui ->
            if (ui is Content) transform(ui) else ui
        }
    }

    /**
     * Инициализация ViewModel.
     * Загружает данные транзакции по [transactionId] и категории по типу [isIncome].
     * @param transactionId ID транзакции для загрузки
     * @param isIncome тип транзакции: доход или расход
     */
    fun init(transactionId: Int, isIncome: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val transactionDef = async { getTransaction(transactionId) }
        val categoriesDef = async { getCategoriesByType(isIncome) }

        val transactionDomain = transactionDef.await().getOrElse { return@launch showError(it) }
        val categoriesDomain = categoriesDef.await().getOrElse { return@launch showError(it) }

        val transaction = transactionMapper.map(transactionDomain)
        val categories = categoriesDomain.map { categoryMapper.map(it) }

        _uiState.value = Content(
            form = TransactionUpdateForm(
                id = transaction.id,
                balance = transaction.accountName,
                selectedCategory = categories.first { category ->
                    category.id == transaction.categoryId
                },
                date = formatIsoDateToHuman(transaction.date),
                time = transaction.time,
                amount = transaction.amount,
                comment = transaction.comment.orEmpty(),
                isIncome = transaction.isIncome
            ),
            categories = categories
        )
    }

    /**
     * Обрабатывает изменение полей формы создания транзакции.
     * @param field изменённое поле формы
     * @param value новое значение поля
     */
    fun onFieldChanged(field: TransactionUpdateField, value: Any) {
        updateContent { content ->
            val f = content.form
            val newForm = when (field) {
                TransactionUpdateField.BALANCE -> f.copy(balance = value as String)
                TransactionUpdateField.CATEGORY -> f.copy(selectedCategory = value as CategoryUiModel)
                TransactionUpdateField.AMOUNT -> f.copy(amount = value as String)
                TransactionUpdateField.COMMENT -> f.copy(comment = value as String)
                TransactionUpdateField.DATE -> f.copy(date = formatLongToHumanDate(value as Long))
                TransactionUpdateField.TIME -> f.copy(time = (value as Pair<Int, Int>).toTimeString())
            }
            content.copy(form = newForm)
        }
    }

    fun closeModal() = _uiState.update { (it as Content).copy(visibleModal = null) }
    fun openModal(modal: TransactionUpdateModal) = _uiState.update {
        (it as Content).copy(visibleModal = modal)
    }

    private fun showSnackbar(@StringRes resId: Int) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _events.emit(TransactionUpdateEvent.ShowSnackBar(resId))
        }
    }

    /**
     * Сохраняет обновлённую транзакцию с [transactionId].
     * Проверяет валидность формы, обновляет состояние UI и обрабатывает результат.
     * @param transactionId ID транзакции для обновления
     */
    fun saveTransaction(transactionId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val content = contentOrNull() ?: return@launch

        val form = content.form

        if (!content.isSaveEnabled) {
            showSnackbar(R.string.amount_error_message)
            return@launch
        }

        _uiState.value = Loading

        val result = updateTransactionUseCase(
            transactionId = transactionId,
            accountId = accountId.value,
            categoryId = form.selectedCategory?.id ?: 0,
            amount = form.amount,
            transactionDate = formatHumanDateToIso(form.date),
            transactionTime = form.time,
            comment = form.comment
        )

        result
            .onSuccess { _events.emit(TransactionUpdateEvent.NavigateBack) }
            .onFailure {
                _uiState.value = content
                showSnackbar(R.string.error_message)
            }
    }

    /**
     * Удаляет транзакцию с указанным [transactionId].
     * Обновляет состояние UI и обрабатывает результат удаления.
     * @param transactionId ID транзакции для удаления
     */
    fun deleteTransaction(transactionId: Int) = viewModelScope.launch(Dispatchers.IO) {
        val content = contentOrNull() ?: return@launch

        _uiState.value = Loading

        val result = deleteTransactionUseCase(transactionId)

        result
            .onSuccess { _events.emit(TransactionUpdateEvent.NavigateBack) }
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

enum class TransactionUpdateField {
    BALANCE,
    CATEGORY,
    AMOUNT,
    COMMENT,
    DATE,
    TIME
}

/** Текущие значения полей формы. */
data class TransactionUpdateForm(
    val id: Int = 0,
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
sealed interface TransactionUpdateModal {
    data object DatePicker : TransactionUpdateModal
    data object TimePicker : TransactionUpdateModal
    data object CategoryPicker : TransactionUpdateModal
}

/** Содержимое snackbar‑сообщения. */
sealed interface TransactionUpdateSnackbar {
    data class Error(@StringRes val messageResId: Int) : TransactionUpdateSnackbar
}

private fun Pair<Int, Int>.toTimeString(): String = "%02d:%02d".format(first, second)