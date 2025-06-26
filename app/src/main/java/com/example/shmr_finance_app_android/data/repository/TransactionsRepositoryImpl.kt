package com.example.shmr_finance_app_android.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shmr_finance_app_android.data.datasource.TransactionsRemoteDataSource
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.domain.mapper.TransactionsDomainMapper
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import javax.inject.Inject

class TransactionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionsRemoteDataSource,
    private val mapper: TransactionsDomainMapper
) : TransactionsRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionDomain>> {
        return safeApiCall {
            remoteDataSource.getTransactionsByPeriod(accountId, startDate, endDate)
                .map(mapper::mapTransaction)
        }
    }
}