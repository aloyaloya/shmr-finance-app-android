package com.example.shmr_finance_app_android.presentation.feature.incomes.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.core.utils.getCurrentDate
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesByPeriodUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.shmr_finance_app_android.presentation.feature.incomes.mapper.TransactionToIncomeMapper
import com.example.shmr_finance_app_android.presentation.feature.incomes.model.IncomeUiModel
import javax.inject.Inject

sealed interface IncomeScreenState {
    data object Loading : IncomeScreenState
    data class Error(val message: String, val retryAction: () -> Unit) : IncomeScreenState
    data object Empty : IncomeScreenState
    data class Success(
        val incomes: List<IncomeUiModel>,
        val totalAmount: String
    ) : IncomeScreenState
}

@HiltViewModel
class IncomeScreenViewModel @Inject constructor(
    private val getTransactionsByPeriodUseCase: GetIncomesByPeriodUseCase,
    private val mapper: TransactionToIncomeMapper
) : ViewModel() {
    private val _screenState = MutableStateFlow<IncomeScreenState>(IncomeScreenState.Loading)
    val screenState: StateFlow<IncomeScreenState> = _screenState.asStateFlow()

    init {
        loadIncomes()
    }

    private fun loadIncomes() {
        _screenState.value = IncomeScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val incomeTransactions = getTransactionsByPeriodUseCase(
                    accountId = 1,
                    startDate = getCurrentDate(),
                    endDate = getCurrentDate()
                )
                if (incomeTransactions.isEmpty()) {
                    _screenState.value = IncomeScreenState.Empty
                } else {
                    _screenState.value = IncomeScreenState.Success(
                        incomes = incomeTransactions.map { mapper.map(it) },
                        totalAmount = mapper.calculateTotalAmount(incomeTransactions)
                    )
                }
            } catch (e: Exception) {
                _screenState.value = IncomeScreenState.Error(
                    message = e.message ?: "Не удалось загрузить ваши доходы",
                    retryAction = { loadIncomes() }
                )
            }
        }
    }
}