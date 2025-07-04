package com.example.shmr_finance_app_android.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shmr_finance_app_android.data.datasource.AccountRemoteDataSource
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.data.repository.mapper.AccountDomainMapper
import com.example.shmr_finance_app_android.domain.model.AccountBriefDomain
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.repository.AccountRepository
import javax.inject.Inject

/**
 * Реализация [AccountRepository], отвечающая за:
 * - Получение данных аккаунта из удаленного источника ([AccountRemoteDataSource])
 * - Изменение валюты счета
 * - Преобразование DTO -> доменную модель ([AccountDomainMapper])
 * - Обработку ошибок через [safeApiCall]
 */
internal class AccountRepositoryImpl @Inject constructor(
    private val remoteDataSource: AccountRemoteDataSource,
    private val mapper: AccountDomainMapper
) : AccountRepository {

    /**
     * Получает данные аккаунта по ID.
     * @param accountId ID аккаунта
     * @return [Result.success] с [AccountDomain] при успехе,
     * [Result.failure] с [AppError] при ошибке
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getAccountById(accountId: Int): Result<AccountDomain> {
        return safeApiCall {
            mapper.mapAccount(remoteDataSource.getAccountById(accountId))
        }
    }

    /**
     * Обновляет данные аккаунта по ID.
     * @param accountBrief [AccountBriefDomain] данных аккаунта
     */
    override suspend fun updateAccountById(accountBrief: AccountBriefDomain) {
        safeApiCall {
            remoteDataSource.updateAccountById(
                accountBrief = mapper.mapAccountBrief(accountBrief)
            )
        }
    }
}