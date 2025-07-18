package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.domain.model.CategoryDomain

/**
 * Репозиторий для работы с данными категорий.
 **/
interface CategoriesRepository {
    suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDomain>>
    suspend fun getAllCategories(): Result<List<CategoryDomain>>
    suspend fun syncCategories(): Result<Unit>
}