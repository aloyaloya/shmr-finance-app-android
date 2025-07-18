package com.example.shmr_finance_app_android.data.repository

import com.example.shmr_finance_app_android.core.network.NetworkChecker
import com.example.shmr_finance_app_android.data.datasources.categories.CategoriesLocalDataSource
import com.example.shmr_finance_app_android.data.datasources.categories.CategoriesRemoteDataSource
import com.example.shmr_finance_app_android.data.local.mapper.CategoryEntityMapper
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.data.repository.mapper.CategoryDomainMapper
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * Реализация [CategoriesRepository], отвечающая за:
 * - Получение данных категорий из удаленного источника ([CategoriesRemoteDataSource])
 * - Преобразование DTO -> доменную модель ([CategoryDomainMapper])
 * - Обработку ошибок через [safeApiCall]
 */
@Reusable
internal class CategoriesRepositoryImpl @Inject constructor(
    private val localDataSource: CategoriesLocalDataSource,
    private val remoteDataSource: CategoriesRemoteDataSource,
    private val mapper: CategoryDomainMapper,
    private val entityMapper: CategoryEntityMapper,
    private val networkChecker: NetworkChecker
) : CategoriesRepository {

    override suspend fun getAllCategories(): Result<List<CategoryDomain>> = runCatching {
        localDataSource.getAllCategories().map {
            val dto = entityMapper.mapCategory(it)
            mapper.mapCategory(dto)
        }
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDomain>> =
        runCatching {
            localDataSource.getCategoriesByType(isIncome).map {
                val dto = entityMapper.mapCategory(it)
                mapper.mapCategory(dto)
            }
        }

    override suspend fun syncCategories(): Result<Unit> = runCatching {
        if (!networkChecker.isNetworkAvailable()) return@runCatching

        val categories = remoteDataSource.getAllCategories()
        val entities = categories.map(entityMapper::mapCategoryToEntity)
        localDataSource.cacheCategories(entities)
    }
}
