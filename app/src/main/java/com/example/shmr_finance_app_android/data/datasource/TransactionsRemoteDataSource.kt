package com.example.shmr_finance_app_android.data.datasource

import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.model.TransactionResponseDTO
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.mapper.TransactionsRemoteMapper
import javax.inject.Inject

/**
 * Отвечает за работу с данными из remote-источника (API).
 * Определяет контракт для работы с данными транзакций без привязки к конкретной реализации.
 */
interface TransactionsRemoteDataSource {
    suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionResponseDTO>

    suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionDTO
}

/**
 * Реализация [TransactionsRemoteDataSource], отвечающая за:
 * - Запрос данных транзакций
 * - Запрос создания создания
 * - Преобразование сетевых моделей в DTO
 *
 * @param api - сервис для работы с API [FinanceApiService]
 * @param mapper - маппер для преобразования сетевых моделей в DTO,
 * либо при для преобразования в данных в тело запроса [TransactionsRemoteMapper]
 */
internal class TransactionsRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: TransactionsRemoteMapper
) : TransactionsRemoteDataSource {

    /**
     * Реализация получения данных транзакций по периоду:
     * 1. Запрашивает данные через API
     * 2. Преобразует результат в DTO
     * 3. Возвращает готовый список DTO
     *
     * @param accountId - ID аккаунта
     * @param startDate - дата начала периода необходимых транзакций
     * (по умолчанию - начало текущего месяца)
     * @param endDate - дата конца периода необходимых транзакций
     * (по умолчанию - конец текущего месяца)
     * @return Список [TransactionResponseDTO] - список преобразованных DTO данных транзакций
     */
    override suspend fun getTransactionsByPeriod(
        accountId: Int,
        startDate: String?,
        endDate: String?
    ): List<TransactionResponseDTO> {
        return api.getTransactionsByPeriod(
            accountId,
            startDate,
            endDate
        ).map(mapper::mapTransactionResponse)
    }

    override suspend fun createTransaction(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionDTO {
        return mapper.mapTransaction(
            api.createTransaction(
                mapper.mapTransactionToRequest(
                    accountId,
                    categoryId,
                    amount,
                    transactionDate,
                    comment
                )
            )
        )
    }
}
