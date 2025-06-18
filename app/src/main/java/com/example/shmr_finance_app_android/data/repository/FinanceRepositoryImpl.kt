package com.example.shmr_finance_app_android.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shmr_finance_app_android.data.datasource.FinanceRemoteDataSource
import com.example.shmr_finance_app_android.domain.mapper.FinanceDomainMapper
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.repository.FinanceRepository
import javax.inject.Inject

class FinanceRepositoryImpl @Inject constructor(
    private val remoteDataSource: FinanceRemoteDataSource,
    private val mapper: FinanceDomainMapper
) : FinanceRepository {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAccountById(accountId: Int): AccountDomain {
        return mapper.mapAccount(remoteDataSource.getAccountById(accountId))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDomain> {
        return remoteDataSource.getTransactionsByPeriod(accountId,startDate, endDate)
            .map(mapper::mapTransaction)
    }

    override suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDomain> {
        return remoteDataSource.getCategoriesByType(isIncome)
            .map(mapper::mapCategory)
    }
}