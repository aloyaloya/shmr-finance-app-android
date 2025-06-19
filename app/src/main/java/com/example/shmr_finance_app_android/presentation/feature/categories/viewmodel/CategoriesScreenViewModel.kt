package com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesCategoriesUseCase
import com.example.shmr_finance_app_android.presentation.feature.categories.mapper.CategoryToIncomeCategoryMapper
import com.example.shmr_finance_app_android.presentation.feature.categories.model.IncomeCategoryUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CategoriesScreenState {
    data object Loading : CategoriesScreenState
    data class Error(val message: String, val retryAction: () -> Unit) : CategoriesScreenState
    data object Empty : CategoriesScreenState
    data class Success(val categories: List<IncomeCategoryUiModel>) : CategoriesScreenState
}

@HiltViewModel
class CategoriesScreenViewModel @Inject constructor(
    private val getIncomeCategories: GetIncomesCategoriesUseCase,
    private val mapper: CategoryToIncomeCategoryMapper
) : ViewModel() {
    private val _screenState = MutableStateFlow<CategoriesScreenState>(CategoriesScreenState.Loading)
    val screenState: StateFlow<CategoriesScreenState> = _screenState.asStateFlow()

    private val _searchRequest = MutableStateFlow("")
    val searchRequest: StateFlow<String> = _searchRequest

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _screenState.value = CategoriesScreenState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val incomeCategories = getIncomeCategories()
                _screenState.value = if (incomeCategories.isEmpty()) {
                    CategoriesScreenState.Empty
                } else {
                    CategoriesScreenState.Success(
                        categories = incomeCategories.map { mapper.map(it) }
                    )
                }
            } catch (e: Exception) {
                _screenState.value = CategoriesScreenState.Error(
                    message = e.message ?: "Неизвестна ошибка",
                    retryAction = { loadCategories() }
                )
            }
        }
    }

    fun onChangeSearchRequest(request: String) {
        _searchRequest.value = request
    }
}