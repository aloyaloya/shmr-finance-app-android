package com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.usecases.GetAccountUseCase
import com.example.shmr_finance_app_android.presentation.feature.balance.mapper.AccountToBalanceMapper
import com.example.shmr_finance_app_android.presentation.feature.balance.model.BalanceUiModel
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel.BalanceScreenState.Error
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel.BalanceScreenState.Loading
import com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel.BalanceScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Состояния экрана Счет с явным разделением:
 * - [Loading] Начальное состояние загрузки
 * - [Error] Состояние ошибки с:
 * - Локализованным сообщением ([messageResId])
 * - Коллбэком повторной попытки ([retryAction])
 * - [Success] - успешное состояние с готовой моделью ([BalanceUiModel])
 */
sealed interface BalanceScreenState {
    data object Loading : BalanceScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : BalanceScreenState
    data class Success(val balance: BalanceUiModel) : BalanceScreenState
}

/**
 * ViewModel для экрана Счет, реализующая:
 * 1. Загрузку данных через [GetAccountUseCase]
 * 2. Преобразование доменной модели в UI-модель через [AccountToBalanceMapper]
 * 3. Управление состояниями экрана ([BalanceScreenState])
 **/
@HiltViewModel
class BalanceScreenViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val mapper: AccountToBalanceMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<BalanceScreenState>(Loading)
    val screenState: StateFlow<BalanceScreenState> = _screenState.asStateFlow()

    private val _showCurrencyBottomSheet = MutableStateFlow(false)
    val showCurrencyBottomSheet: StateFlow<Boolean> = _showCurrencyBottomSheet.asStateFlow()

    init {
        loadBalanceInfo()
    }

    /**
     * Загружает данные счета, управляя состояниями:
     * 1. [Loading] - перед запросом
     * 2. [Success] или [Error] - после получения результата
     */
    private fun loadBalanceInfo() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleBalanceResult(getAccount(accountId = 1))
        }
    }

    /**
     * Обрабатывает результат запроса, преобразуя:
     * - Успех -> [BalanceUiModel] через маппер
     * - Ошибку -> Сообщение об ошибке
     */
    private fun handleBalanceResult(result: Result<AccountDomain>) {
        result
            .onSuccess { data -> handleSuccess(mapper.map(data)) }
            .onFailure { error -> handleError(error) }
    }

    /** Обновляет состояние при успешной загрузке */
    private fun handleSuccess(data: BalanceUiModel) {
        _screenState.value = Success(data)
    }

    /** Обрабатывает ошибку */
    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadBalanceInfo() }
        )
    }

    fun onBalanceCurrencySelected(currency: String) {
    }

    fun onDismissBalanceCurrency() {
        _showCurrencyBottomSheet.value = false
    }

    fun onShowBalanceCurrency() {
        _showCurrencyBottomSheet.value = true
    }
}
