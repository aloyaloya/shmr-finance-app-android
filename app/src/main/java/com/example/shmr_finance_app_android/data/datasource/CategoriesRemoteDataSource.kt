package com.example.shmr_finance_app_android.data.datasource

import com.example.shmr_finance_app_android.data.model.CategoryDTO
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.mapper.CategoryRemoteMapper
import javax.inject.Inject

interface CategoriesRemoteDataSource {
    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDTO>
}

class CategoriesRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: CategoryRemoteMapper
) : CategoriesRemoteDataSource {
    override suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDTO> {
        return api.getCategoriesByType(isIncome)
            .map(mapper::mapCategory)
    }
}