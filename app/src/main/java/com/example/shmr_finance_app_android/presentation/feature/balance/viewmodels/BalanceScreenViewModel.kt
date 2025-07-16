package com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.BuildConfig
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.GetAccountUseCase
import com.example.shmr_finance_app_android.presentation.feature.balance.mappers.AccountToBalanceMapper
import com.example.shmr_finance_app_android.presentation.feature.balance.models.BalanceUiModel
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels.BalanceUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels.BalanceUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodels.BalanceUiState.Loading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI‑state экрана счета.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */
sealed interface BalanceUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : BalanceUiState

    /** Контентный стейт, когда все данные загружены */
    data class Content(val balance: BalanceUiModel) : BalanceUiState

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : BalanceUiState
}

/**
 * ViewModel для экрана Счет, реализующая:
 * 1. Загрузку данных через [GetAccountUseCase]
 * 2. Преобразование доменной модели в UI-модель через [AccountToBalanceMapper]
 * 3. Управление состояниями экрана ([BalanceUiState])
 **/
class BalanceScreenViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val mapper: AccountToBalanceMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<BalanceUiState>(Loading)
    val uiState: StateFlow<BalanceUiState> = _uiState.asStateFlow()

    private val accountId = MutableStateFlow(BuildConfig.ACCOUNT_ID)

    /**
     * Загружает данные счета, управляя состояниями:
     * 1. [Loading] - перед запросом
     * 2. [Success] или [Error] - после получения результата
     */
    fun init() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val result = getAccount(accountId = accountId.value)

        result
            .onSuccess { data -> _uiState.value = Content(mapper.map(data)) }
            .onFailure { error -> showError(error) }
    }

    /** Обработчик для показа ошибки */
    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }

    /** Получает ID аккаунта */
    fun getAccountId(): Int = accountId.value
}
