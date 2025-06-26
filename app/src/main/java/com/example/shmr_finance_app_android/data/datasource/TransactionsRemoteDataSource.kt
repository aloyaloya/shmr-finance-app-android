package com.example.shmr_finance_app_android.data.datasource

import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.mapper.TransactionsRemoteMapper
import javax.inject.Inject

interface TransactionsRemoteDataSource {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDTO>
}

class TransactionsRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: TransactionsRemoteMapper
) : TransactionsRemoteDataSource {
    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionDTO> {
        return api.getTransactionsByPeriod(
            accountId,
            startDate,
            endDate
        ).map(mapper::mapTransaction)
    }
}
