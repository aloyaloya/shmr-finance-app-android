package com.example.shmr_finance_app_android.data.datasources.categories

import com.example.shmr_finance_app_android.data.local.dao.CategoryDao
import com.example.shmr_finance_app_android.data.local.entities.CategoryEntity
import com.example.shmr_finance_app_android.data.local.mapper.CategoryEntityMapper
import javax.inject.Inject

/**
 * Реализация [CategoriesLocalDataSource], отвечающая за:
 * - Запрос данных категорий из базы данных
 * - Преобразование Entity моделей в DTO
 *
 * @param dao - dao для работы с локальной базой данных [CategoryDao]
 * @param mapper - маппер для преобразования Entity моделей [CategoryEntityMapper]
 */
class CategoriesLocalDataSourceImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoriesLocalDataSource {

    override suspend fun getAllCategories(): List<CategoryEntity> {
        return dao.getAll()
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryEntity> {
        return dao.getByType(isIncome)
    }

    override suspend fun getCategoryById(id: Int): CategoryEntity? {
        return dao.getById(id)
    }

    override suspend fun cacheCategories(categories: List<CategoryEntity>) {
        dao.insertAll(categories)
    }
}