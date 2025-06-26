package com.example.shmr_finance_app_android.data.repository

import com.example.shmr_finance_app_android.data.datasource.CategoriesRemoteDataSource
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.data.repository.mapper.CategoryDomainMapper
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import javax.inject.Inject

internal class CategoriesRepositoryImpl @Inject constructor(
    private val remoteDataSource: CategoriesRemoteDataSource,
    private val mapper: CategoryDomainMapper
) : CategoriesRepository {
    override suspend fun getCategoriesByType(isIncome: Boolean): Result<List<CategoryDomain>> {
        return safeApiCall {
            remoteDataSource.getCategoriesByType(isIncome)
                .map(mapper::mapCategory)
        }
    }
}