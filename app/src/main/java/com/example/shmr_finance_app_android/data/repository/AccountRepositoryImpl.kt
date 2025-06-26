package com.example.shmr_finance_app_android.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shmr_finance_app_android.data.datasource.AccountRemoteDataSource
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.domain.mapper.AccountDomainMapper
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.repository.AccountRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountRemoteDataSource,
    private val mapper: AccountDomainMapper
) : AccountRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAccountById(accountId: Int): Result<AccountDomain> {
        return safeApiCall {
            mapper.mapAccount(remoteDataSource.getAccountById(accountId))
        }
    }
}