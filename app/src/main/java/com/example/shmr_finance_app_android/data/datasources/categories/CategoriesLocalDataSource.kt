package com.example.shmr_finance_app_android.data.datasources.categories

import com.example.shmr_finance_app_android.data.local.entities.CategoryEntity

/**
 * Отвечает за получение категорий из локальной базы данных.
 * Определяет контракт для работы с данными категорий без привязки к конкретной реализации.
 */
interface CategoriesLocalDataSource {
    suspend fun getAllCategories(): List<CategoryEntity>
    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryEntity>
    suspend fun getCategoryById(id: Int): CategoryEntity?
    suspend fun cacheCategories(categories: List<CategoryEntity>)
}