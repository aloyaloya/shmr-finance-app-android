package com.example.shmr_finance_app_android.data.datasources.categories

import com.example.shmr_finance_app_android.data.model.CategoryDTO

/**
 * Отвечает за получение категорий из remote-источника (API).
 * Определяет контракт для работы с данными категорий без привязки к конкретной реализации.
 */
interface CategoriesRemoteDataSource {
    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDTO>
    suspend fun getAllCategories(): List<CategoryDTO>
}