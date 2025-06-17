package com.example.shmr_finance_app_android.ui.screens.balance_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.domain.model.Balance
import com.example.shmr_finance_app_android.data.remote.api.model.mockBalance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BalanceScreenState {
    object Loading : BalanceScreenState
    data class Error(val message: String, val retryAction: () -> Unit) : BalanceScreenState
    data class Success(val balance: Balance) : BalanceScreenState
}

@HiltViewModel
class BalanceScreenViewModel @Inject constructor() : ViewModel() {
    private val _screenState = MutableStateFlow<BalanceScreenState>(BalanceScreenState.Loading)
    val screenState: StateFlow<BalanceScreenState> = _screenState.asStateFlow()

    init {
        loadBalanceInfo()
    }

    private fun loadBalanceInfo() {
        _screenState.value = BalanceScreenState.Loading
        viewModelScope.launch {
            try {
                val balance = mockBalance
                _screenState.value = BalanceScreenState.Success(
                    balance = balance
                )
            } catch (e: Exception) {
                _screenState.value = BalanceScreenState.Error(
                    message = e.message ?: "Неизвестна ошибка",
                    retryAction = { loadBalanceInfo() }
                )
            }
        }
    }
}