package com.example.shmr_finance_app_android.data.remote.datasource

import com.example.shmr_finance_app_android.data.dto.AccountDTO
import com.example.shmr_finance_app_android.data.dto.CategoryDTO
import com.example.shmr_finance_app_android.data.dto.TransactionDTO
import com.example.shmr_finance_app_android.data.mapper.FinanceRemoteMapper
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import javax.inject.Inject

class FinanceRemoteDataSource @Inject constructor(
    private val apiService: FinanceApiService,
    private val mapper: FinanceRemoteMapper
) {
    suspend fun getAccountById(accountId: Int): AccountDTO {
        return mapper.mapAccount(apiService.getAccountById(accountId))
    }

    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): List<TransactionDTO> {
        return apiService.getTransactionsByPeriod(
            accountId,
            startDate,
            endDate
        ).map(mapper::mapTransaction)
    }

    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDTO> {
        return apiService.getCategoriesByType(isIncome)
            .map(mapper::mapCategory)
    }
}