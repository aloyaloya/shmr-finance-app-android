package com.example.shmr_finance_app_android.presentation.feature.balance.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.remote.api.AppError
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
    data class Error(val message: String, val retryAction: () -> Unit) : BalanceScreenState
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
            val balanceInfo = getAccount(accountId = 1)
            balanceInfo.onSuccess { data ->
                _screenState.value = BalanceScreenState.Success(
                    balance = mapper.map(data)
                )
            }.onFailure { error ->
                _screenState.value = BalanceScreenState.Error(
                    message = when (error as? AppError) {
                        is AppError.Network -> R.string.network_error.toString()
                        is AppError.ApiError -> "${R.string.network_error} ${error.message}"
                        is AppError.Unknown -> R.string.unknown_error.toString()
                        null -> R.string.unknown_error.toString()
                    },
                    retryAction = { loadBalanceInfo() }
                )
            }
        }
    }
}