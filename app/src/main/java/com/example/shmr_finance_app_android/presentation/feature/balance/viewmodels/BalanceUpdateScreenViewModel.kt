package com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.GetAccountUseCase
import com.example.shmr_finance_app_android.domain.usecases.UpdateAccountUseCase
import com.example.shmr_finance_app_android.presentation.feature.balance.mappers.AccountToBalanceDetailedMapper
import com.example.shmr_finance_app_android.presentation.feature.balance.mappers.AccountToBalanceMapper
import com.example.shmr_finance_app_android.presentation.feature.balance.models.CurrencyItem
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels.BalanceUpdateUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels.BalanceUpdateUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels.BalanceUpdateUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI‑state экрана редактирования счета.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */
sealed interface BalanceUpdateUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : BalanceUpdateUiState

    /**
     * Контентный стейт, когда все данные загружены
     * и пользователь может взаимодействовать с формой.
     */
    data class Content(
        val form: BalanceUpdateForm = BalanceUpdateForm(),
        val visibleModal: BalanceUpdateModal? = null,
        val snackbar: BalanceUpdateSnackbar? = null
    ) : BalanceUpdateUiState {
        /** Валиден ли ввод и активна ли кнопка Сохранить. */
        val isSaveEnabled: Boolean
            get() =
                form.amount.toIntOrNull() != null && form.name.isNotEmpty()
    }

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : BalanceUpdateUiState
}

/** Эвенты Snackbar + Navigate */
sealed interface BalanceUpdateEvent {
    data class ShowSnackBar(@StringRes val messageResId: Int) : BalanceUpdateEvent
    data object NavigateBack : BalanceUpdateEvent
}

/**
 * ViewModel для экрана счета, реализующая:
 * 1. Загрузку данных через [GetAccountUseCase]
 * 2. Обновление данных через [UpdateAccountUseCase]
 * 3. Преобразование доменной модели в UI-модель через [AccountToBalanceMapper]
 * 4. Управление состояниями экрана ([BalanceUpdateUiState])
 **/
class BalanceUpdateScreenViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val updateBalance: UpdateAccountUseCase,
    private val mapper: AccountToBalanceDetailedMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<BalanceUpdateUiState>(Loading)
    val uiState: StateFlow<BalanceUpdateUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<BalanceUpdateEvent>()
    val events: SharedFlow<BalanceUpdateEvent> = _events

    private fun contentOrNull() = _uiState.value as? Content

    /**
     * Обновляет текущее состояние UI, применяя функцию преобразования к состоянию с типом [Content].
     * @param transform функция, которая принимает текущее состояние [Content] и возвращает обновлённое
     */
    private fun updateContent(transform: (Content) -> Content) {
        _uiState.update { ui ->
            if (ui is Content) transform(ui) else ui
        }
    }

    /**
     * Инициализирует ViewModel, загружая данные баланса по его ID.
     * Устанавливает состояние загрузки, затем получает данные по [balanceId] и обновляет состояние UI.
     * @param balanceId строковый ID баланса, который будет преобразован в Int
     */
    fun init(balanceId: String) = viewModelScope.launch {
        _uiState.value = Loading

        val result = getAccount(accountId = balanceId.toInt())

        result
            .onSuccess { domain ->
                val data = mapper.map(domain)
                _uiState.value = Content(
                    form = BalanceUpdateForm(
                        name = data.name,
                        amount = data.amount,
                        currency = CurrencyItem.items.first {
                            it.currencyCode == data.currencyCode
                        }
                    )
                )
            }
            .onFailure { error -> showError(error) }
    }

    /**
     * Обрабатывает изменение значения в форме обновления баланса.
     * @param field поле формы, которое изменилось
     * @param value новое значение поля
     */
    fun onFieldChanged(field: BalanceUpdateField, value: Any) {
        updateContent { content ->
            val f = content.form
            val newForm = when (field) {
                BalanceUpdateField.NAME -> f.copy(name = value as String)
                BalanceUpdateField.AMOUNT -> f.copy(amount = value as String)
                BalanceUpdateField.CURRENCY -> f.copy(currency = value as CurrencyItem)
            }
            content.copy(form = newForm)
        }
    }

    fun closeModal() = _uiState.update { (it as Content).copy(visibleModal = null) }
    fun openModal(modal: BalanceUpdateModal) = _uiState.update {
        (it as Content).copy(visibleModal = modal)
    }

    /**
     * Сохраняет обновлённый баланс, если форма валидна.
     * Запускает процесс обновления баланса в IO-потоке, обновляет состояние UI,
     * при успешном результате инициирует навигацию назад, иначе показывает ошибку.
     * @param balanceId строковый ID баланса, преобразуемый в Int
     */
    fun saveBalance(balanceId: String) = viewModelScope.launch(Dispatchers.IO) {
        val content = contentOrNull() ?: return@launch

        val form = content.form

        if (!content.isSaveEnabled) {
            showSnackbar(R.string.balance_data_error_message)
            return@launch
        }

        _uiState.value = Loading

        val result = updateBalance(
            accountId = balanceId.toInt(),
            accountName = form.name,
            accountBalance = form.amount.toInt(),
            accountCurrency = form.currency?.currencyCode ?: "RUB"
        )

        result
            .onSuccess { _events.emit(BalanceUpdateEvent.NavigateBack) }
            .onFailure {
                _uiState.value = content
                showSnackbar(R.string.error_message)
            }
    }

    /** Обработчик для показа snackbar с сообщением об ошибке */
    private fun showSnackbar(@StringRes resId: Int) {
        viewModelScope.launch(Dispatchers.Main.immediate) {
            _events.emit(BalanceUpdateEvent.ShowSnackBar(resId))
        }
    }

    /** Обработчик для показа ошибки */
    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}

enum class BalanceUpdateField {
    NAME,
    AMOUNT,
    CURRENCY
}

/** Текущие значения полей формы. */
data class BalanceUpdateForm(
    val name: String = "",
    val amount: String = "",
    val currency: CurrencyItem? = null
)

/** Какое модальное окно сейчас открыто (если открыто). */
sealed interface BalanceUpdateModal {
    data object CurrencyPicker : BalanceUpdateModal
}

/** Содержимое snackbar‑сообщения. */
sealed interface BalanceUpdateSnackbar {
    data class Error(@StringRes val messageResId: Int) : BalanceUpdateSnackbar
}