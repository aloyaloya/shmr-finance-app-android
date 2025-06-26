package com.example.shmr_finance_app_android.domain.repository

import com.example.shmr_finance_app_android.domain.model.CategoryDomain

interface CategoriesRepository {
    suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDomain>>
}