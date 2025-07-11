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

    /**
     * Создаёт новую транзакцию с указанными параметрами и возвращает результат операции.
     *
     * @param accountId ID счёта, к которому относится транзакция
     * @param categoryId ID категории транзакции
     * @param amount Сумма транзакции в виде строки
     * @param transactionDate Дата транзакции в формате строки
     * @param comment Необязательный комментарий к транзакции
     * @return Результат с доменной моделью [TransactionDomain] созданной транзакции или ошибкой
     */
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


    /**
     * Получает транзакцию по её ID и возвращает результат операции.
     *
     * @param transactionId ID транзакции для получения
     * @return Результат с доменной моделью [TransactionResponseDomain] ответа или ошибкой
     */
    override suspend fun getTransactionById(transactionId: Int): Result<TransactionResponseDomain> {
        return safeApiCall(
            call = {
                mapper.mapTransactionResponse(
                    remoteDataSource.getTransactionById(transactionId)
                )
            }
        )
    }


    /**
     * Обновляет транзакцию по ID с новыми параметрами и возвращает результат операции.
     *
     * @param transactionId ID транзакции для обновления
     * @param accountId ID счёта, связанного с транзакцией
     * @param categoryId ID категории транзакции
     * @param amount Новая сумма транзакции
     * @param transactionDate Новая дата транзакции
     * @param comment Новый комментарий (может быть null)
     * @return Результат с доменной моделью [TransactionResponseDomain] обновлённой транзакции
     * или ошибкой
     */
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

    /**
     * Удаляет транзакцию по её ID и возвращает результат операции.
     *
     * @param transactionId ID транзакции для удаления
     * @return Результат успешного выполнения или ошибки
     */
    override suspend fun deleteTransactionById(transactionId: Int): Result<Unit> {
        return safeApiCall(
            call = { remoteDataSource.deleteTransactionById(transactionId) },
            handleSuccess = {
                Result.success(Unit)
            }
        )
    }
}