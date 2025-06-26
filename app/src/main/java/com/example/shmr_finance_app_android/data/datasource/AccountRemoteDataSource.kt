package com.example.shmr_finance_app_android.data.datasource

import com.example.shmr_finance_app_android.data.model.AccountDTO
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.mapper.AccountRemoteMapper
import javax.inject.Inject

interface AccountRemoteDataSource {
    suspend fun getAccountById(accountId: Int): AccountDTO
}

internal class AccountRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: AccountRemoteMapper
) : AccountRemoteDataSource {

    override suspend fun getAccountById(accountId: Int): AccountDTO {
        return mapper.mapAccount(api.getAccountById(accountId))
    }
}