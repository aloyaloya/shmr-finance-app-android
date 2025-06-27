package com.example.shmr_finance_app_android.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shmr_finance_app_android.data.datasource.TransactionsRemoteDataSource
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.data.repository.mapper.TransactionsDomainMapper
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import javax.inject.Inject

/**
 * Реализация [TransactionsRepository], отвечающая за:
 * - Получение данных транзакций из удаленного источника ([TransactionsRemoteDataSource])
 * - Преобразование DTO -> доменную модель ([TransactionsDomainMapper])
 * - Обработку ошибок через [safeApiCall]
 */
internal class TransactionsRepositoryImpl @Inject constructor(
    private val remoteDataSource: TransactionsRemoteDataSource,
    private val mapper: TransactionsDomainMapper
) : TransactionsRepository {

    /**
     * Получает данные аккаунта по ID.
     * @param accountId - ID аккаунта
     * @param startDate - дата начала периода необходимых транзакций
     * (по умолчанию - начало текущего месяца)
     * @param endDate - дата конца периода необходимых транзакций
     * (по умолчанию - конец текущего месяца)
     * @return [Result.success] с [TransactionDomain] при успехе,
     * [Result.failure] с [AppError] при ошибке
     */
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