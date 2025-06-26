package com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.usecases.GetAccountUseCase
import com.example.shmr_finance_app_android.presentation.feature.balance.mapper.AccountToBalanceMapper
import com.example.shmr_finance_app_android.presentation.feature.balance.model.BalanceUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BalanceScreenState {
    data object Loading : BalanceScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : BalanceScreenState
    data class Success(val balance: BalanceUiModel) : BalanceScreenState
}

@HiltViewModel
class BalanceScreenViewModel @Inject constructor(
    private val getAccount: GetAccountUseCase,
    private val mapper: AccountToBalanceMapper
) : ViewModel() {

    private val _screenState = MutableStateFlow<BalanceScreenState>(BalanceScreenState.Loading)
    val screenState: StateFlow<BalanceScreenState> = _screenState.asStateFlow()

    init {
        loadBalanceInfo()
    }

    private fun loadBalanceInfo() {
        _screenState.value = BalanceScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleBalanceResult(getAccount(accountId = 1))
        }
    }

    private fun handleBalanceResult(result: Result<AccountDomain>) {
        result
            .onSuccess { data -> handleSuccess(mapper.map(data)) }
            .onFailure { error -> handleError(error) }
    }

    private fun handleSuccess(data: BalanceUiModel) {
        _screenState.value = BalanceScreenState.Success(data)
    }

    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = BalanceScreenState.Error(
            messageResId = messageResId,
            retryAction = { loadBalanceInfo() }
        )
    }
}