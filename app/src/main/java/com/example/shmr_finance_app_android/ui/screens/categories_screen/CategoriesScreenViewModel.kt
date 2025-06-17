package com.example.shmr_finance_app_android.ui.screens.categories_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.data.model.domain.Category
import com.example.shmr_finance_app_android.data.model.mockCategories
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface CategoriesScreenState {
    object Loading : CategoriesScreenState
    data class Error(val message: String, val retryAction: () -> Unit) : CategoriesScreenState
    object Empty : CategoriesScreenState
    data class Success(val categories: List<Category>) : CategoriesScreenState
}

@HiltViewModel
class CategoriesScreenViewModel @Inject constructor() : ViewModel() {
    private val _screenState = MutableStateFlow<CategoriesScreenState>(CategoriesScreenState.Loading)
    val screenState: StateFlow<CategoriesScreenState> = _screenState.asStateFlow()

    private val _searchRequest = MutableStateFlow("")
    val searchRequest: StateFlow<String> = _searchRequest

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _screenState.value = CategoriesScreenState.Loading
        viewModelScope.launch {
            try {
                val categories = mockCategories
                _screenState.value = if (categories.isEmpty()) {
                    CategoriesScreenState.Empty
                } else {
                    CategoriesScreenState.Success(
                        categories = categories
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