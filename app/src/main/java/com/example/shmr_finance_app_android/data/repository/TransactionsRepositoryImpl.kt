package com.example.shmr_finance_app_android.data.repository

import com.example.shmr_finance_app_android.data.datasource.TransactionsRemoteDataSource
import com.example.shmr_finance_app_android.data.remote.api.safeApiCall
import com.example.shmr_finance_app_android.data.repository.mapper.TransactionsDomainMapper
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
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
     * @return [Result.success] с [TransactionResponseDomain] при успехе,
     * [Result.failure] с [AppError] при ошибке
     */
    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): Result<List<TransactionResponseDomain>> {
        return safeApiCall(
            call = {
                remoteDataSource.getTransactionsByPeriod(accountId, startDate, endDate)
                    .map(mapper::mapTransactionResponse)
            }
        )
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionDomain> {
        return safeApiCall(
            call = {
                mapper.mapTransaction(
                    remoteDataSource.createTransaction(
                        accountId,
                        categoryId,
                        amount,
                        transactionDate,
                        comment
                    )
                )
            }
        )
    }

    override suspend fun getTransactionById(transactionId: Int): Result<TransactionResponseDomain> {
        return safeApiCall(
            call = {
                mapper.mapTransactionResponse(
                    remoteDataSource.getTransactionById(transactionId)
                )
            }
        )
    }

    override suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): Result<TransactionResponseDomain> {
        return safeApiCall(
            call = {
                mapper.mapTransactionResponse(
                    remoteDataSource.updateTransactionById(
                        transactionId = transactionId,
                        accountId = accountId,
                        categoryId = categoryId,
                        amount = amount,
                        transactionDate = transactionDate,
                        comment = comment
                    )
                )
            }
        )
    }

    override suspend fun deleteTransactionById(transactionId: Int): Result<Unit> {
        return safeApiCall(
            call = { remoteDataSource.deleteTransactionById(transactionId) },
            handleSuccess = {
                Result.success(Unit)
            }
        )
    }
}