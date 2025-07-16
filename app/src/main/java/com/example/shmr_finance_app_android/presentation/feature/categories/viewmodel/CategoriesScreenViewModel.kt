package com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.domain.usecases.GetCategoriesUseCase
import com.example.shmr_finance_app_android.presentation.feature.categories.mapper.CategoryToCategoryUiMapper
import com.example.shmr_finance_app_android.presentation.feature.categories.model.CategoryUiModel
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesUiState.Content
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesUiState.Empty
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesUiState.Error
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesUiState.Loading
import com.example.shmr_finance_app_android.presentation.feature.categories.viewmodel.CategoriesUiState.SearchEmpty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * UI‑state экрана статьи.
 * 1. [Loading] Состояние загрузки
 * 2. [Content] Состояние взаимодействия с пользователем
 * 3. [Error] Состояние ошибки загрузки
 * 4. [Empty] Состояние при получении пустого списка категорий
 * 3. [SearchEmpty] Состояние при получении пустого списка категорий при поиске
 * Содержит только данные, которые необходимы Compose‑слою для отрисовки.
 */

sealed interface CategoriesUiState {

    /** Экран в процессе начальной загрузки данных. */
    data object Loading : CategoriesUiState

    /**
     * Контентный стейт, когда все данные загружены
     * и пользователь может взаимодействовать с формой.
     */
    data class Content(val categories: List<CategoryUiModel>) : CategoriesUiState

    /** Экран при получении пустых данных. */
    data object Empty : CategoriesUiState

    /** Экран при получении пустых данных при поиске. */
    data object SearchEmpty : CategoriesUiState

    /** Фатальная ошибка получения данных. */
    data class Error(@StringRes val messageResId: Int) : CategoriesUiState
}

/**
 * ViewModel для экрана Статьи, реализующая:
 * 1. Загрузку данных через [getCategories]
 * 2. Преобразование доменной модели в UI-модель через [CategoryToCategoryUiMapper]
 * 3. Управление состояниями экрана ([CategoriesUiState])
 **/
class CategoriesScreenViewModel @Inject constructor(
    private val getCategories: GetCategoriesUseCase,
    private val mapper: CategoryToCategoryUiMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow<CategoriesUiState>(Loading)
    val uiState: StateFlow<CategoriesUiState> = _uiState.asStateFlow()

    private var cachedCategories: List<CategoryUiModel> = emptyList()

    private val _searchRequest = MutableStateFlow("")
    val searchRequest: StateFlow<String> = _searchRequest

    /**
     * Загружает данные о статьях, управляя состояниями:
     * 1. [Loading] - перед запросом
     * 2. [Success] или [Error] - после получения результата
     */
    fun init() = viewModelScope.launch(Dispatchers.IO) {
        _uiState.value = Loading

        val result = getCategories()

        result
            .onSuccess { data ->
                cachedCategories = data.map { mapper.map(it) }
                updateState()
            }
            .onFailure { error -> showError(error) }
    }

    /**
     * Обновляет состояние экрана [CategoriesUiState]
     * Поведение:
     * 1. Проверяет, введен ли поисковой запрос
     * 2. Выдает полученные/отфильтрованные комнаты
     * 3. Изменяет состояние экрана
     * */
    fun updateState() {
        val query = _searchRequest.value
        val filtered = when {
            query.isBlank() -> cachedCategories
            else -> cachedCategories.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _uiState.value = when {
            filtered.isEmpty() && query.isNotBlank() -> SearchEmpty
            filtered.isEmpty() -> Empty
            else -> Content(categories = filtered)
        }
    }

    /** Изменение поискового запроса */
    fun onChangeSearchRequest(request: String) {
        _searchRequest.value = request
        updateState()
    }

    /** Обработчик для показа ошибки */
    private fun showError(t: Throwable) {
        val res = (t as? AppError)?.messageResId ?: R.string.unknown_error
        _uiState.value = Error(messageResId = res)
    }
}