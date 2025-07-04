package com.example.shmr_finance_app_android.data.repository

import com.example.shmr_finance_app_android.data.datasource.CategoriesRemoteDataSource
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.data.repository.mapper.CategoryDomainMapper
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import javax.inject.Inject

/**
 * Реализация [CategoriesRepository], отвечающая за:
 * - Получение данных категорий из удаленного источника ([CategoriesRemoteDataSource])
 * - Преобразование DTO -> доменную модель ([CategoryDomainMapper])
 * - Обработку ошибок через [safeApiCall]
 */
internal class CategoriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource,
    private val mapper: CategoryDomainMapper
) : CategoriesRepository {

    /**
     * Получает данные категорий по типу (доходы, расходы).
     * @param isIncome - 'true' - доходы, 'false' - расходы
     * @return [Result.success] с [CategoryDomain] при успехе,
     * [Result.failure] с [AppError] при ошибке
     */
    override suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDomain>> {
        return safeApiCall {
            remoteDataSource.getCategoriesByType(isIncome)
                .map(mapper::mapCategory)
        }
    }

    /**
     * Получает данные всех категорий.
     *
     * @return [Result.success] с [CategoryDomain] при успехе,
     * [Result.failure] с [AppError] при ошибке
     */
    override suspend fun getAllCategories(): Result<List<CategoryDomain>> {
        return safeApiCall {
            remoteDataSource.getAllCategories()
                .map(mapper::mapCategory)
        }
    }
}