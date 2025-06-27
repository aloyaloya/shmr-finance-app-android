package com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesCategoriesUseCase
import com.example.shmr_finance_app_android.presentation.feature.categories.mapper.CategoryToIncomeCategoryMapper
import com.example.shmr_finance_app_android.presentation.feature.categories.model.IncomeCategoryUiModel
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenState.Error
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenState.Loading
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesScreenState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Состояния экрана Статьи с явным разделением:
 * - [Loading] Начальное состояние загрузки
 * - [Error] Состояние ошибки с:
 * - Локализованным сообщением ([messageResId])
 * - Коллбэком повторной попытки ([retryAction])
 * - [Success] - успешное состояние с списком готовых моделей ([IncomeCategoryUiModel])
 */
sealed interface CategoriesScreenState {
    data object Loading : CategoriesScreenState
    data class Error(val messageResId: Int, val retryAction: () -> Unit) : CategoriesScreenState
    data object Empty : CategoriesScreenState
    data class Success(val categories: List<IncomeCategoryUiModel>) : CategoriesScreenState
}

/**
 * ViewModel для экрана Статьи, реализующая:
 * 1. Загрузку данных через [getIncomeCategories]
 * 2. Преобразование доменной модели в UI-модель через [CategoryToIncomeCategoryMapper]
 * 3. Управление состояниями экрана ([CategoriesScreenState])
 **/
@HiltViewModel
class CategoriesScreenViewModel @Inject constructor(
    private val getIncomeCategories: GetIncomesCategoriesUseCase,
    private val mapper: CategoryToIncomeCategoryMapper
) : ViewModel() {

    private val _screenState =
        MutableStateFlow<CategoriesScreenState>(Loading)
    val screenState: StateFlow<CategoriesScreenState> = _screenState.asStateFlow()

    private val _searchRequest = MutableStateFlow("")
    val searchRequest: StateFlow<String> = _searchRequest

    init {
        loadCategories()
    }

    /**
     * Загружает данные о статьях, управляя состояниями:
     * 1. [Loading] - перед запросом
     * 2. [Success] или [Error] - после получения результата
     */
    private fun loadCategories() {
        _screenState.value = Loading
        viewModelScope.launch(Dispatchers.IO) {
            handleCategoriesResult(getIncomeCategories())
        }
    }

    /**
     * Обрабатывает результат запроса, преобразуя:
     * - Успех -> [IncomeCategoryUiModel] через маппер
     * - Ошибку -> Сообщение об ошибке
     */
    private fun handleCategoriesResult(result: Result<List<CategoryDomain>>) {
        result
            .onSuccess { data -> handleSuccess(data.map { mapper.map(it) }) }
            .onFailure { error -> handleError(error) }
    }

    /** Обновляет состояние при успешной загрузке */
    private fun handleSuccess(data: List<IncomeCategoryUiModel>) {
        _screenState.value = Success(data)
    }

    /** Обрабатывает ошибку */
    private fun handleError(error: Throwable) {
        val messageResId = (error as? AppError)?.messageResId ?: R.string.unknown_error
        _screenState.value = Error(
            messageResId = messageResId,
            retryAction = { loadCategories() }
        )
    }

    /** Изменение поискового запроса */
    fun onChangeSearchRequest(request: String) {
        _searchRequest.value = request
    }
}