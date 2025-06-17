package com.example.shmr_finance_app_android.ui.screens.income_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.data.model.domain.Income
import com.example.shmr_finance_app_android.data.model.mockIncomes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface IncomeScreenState {
    object Loading : IncomeScreenState
    data class Error(val message: String, val retryAction: () -> Unit) : IncomeScreenState
    object Empty : IncomeScreenState
    data class Success(
        val incomes: List<Income>
    ) : IncomeScreenState
}

@HiltViewModel
class IncomeScreenViewModel @Inject constructor() : ViewModel() {
    private val _screenState = MutableStateFlow<IncomeScreenState>(IncomeScreenState.Loading)
    val screenState: StateFlow<IncomeScreenState> = _screenState.asStateFlow()

    init {
        loadIncomes()
    }

    private fun loadIncomes() {
        _screenState.value = IncomeScreenState.Loading
        viewModelScope.launch {
            try {
                val incomes = mockIncomes
                _screenState.value = if (incomes.isEmpty()) {
                    IncomeScreenState.Empty
                } else {
                    IncomeScreenState.Success(
                        incomes = incomes
                    )
                }
            } catch (e: Exception) {
                _screenState.value = IncomeScreenState.Error(
                    message = e.message ?: "Неизвестна ошибка",
                    retryAction = { loadIncomes() }
                )
            }
        }
    }
}