package com.example.shmr_finance_app_android.data.datasources.transactions

import android.util.Log
import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.model.TransactionResponseDTO
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.mapper.TransactionsRemoteMapper
import retrofit2.Response
import javax.inject.Inject

/**
 * Реализация [TransactionsRemoteDataSource], отвечающая за:
 * - Запрос данных транзакций
 * - Запрос создания создания
 * - Запрос удаления транзакций
 * - Запрос изменения данных транзакций
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
        val result = api.getTransactionsByPeriod(
            accountId,
            startDate,
            endDate
        ).map(mapper::mapTransactionResponse)

        Log.i("ERROR", result.toString())
        return result
    }

    /** Получает транзакцию по ID и мапит её в [TransactionResponseDTO]. */
    override suspend fun getTransactionById(transactionId: Int): TransactionResponseDTO {
        return mapper.mapTransactionResponse(api.getTransactionById(transactionId))
    }

    /**
     * Создаёт новую транзакцию с заданными параметрами.
     *
     * @param accountId ID счёта, к которому относится транзакция
     * @param categoryId ID категории транзакции (доход/расход и т.п.)
     * @param amount Сумма транзакции в виде строки
     * @param transactionDate Дата транзакции в формате строки
     * @param comment Необязательный комментарий к транзакции
     * @return [TransactionDTO] с данными созданной транзакции
     */
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

    /**
     * Обновляет существующую транзакцию по её ID.
     *
     * @param transactionId ID обновляемой транзакции
     * @param accountId ID счёта, связанного с транзакцией
     * @param categoryId ID категории транзакции
     * @param amount Новая сумма транзакции
     * @param transactionDate Новая дата транзакции
     * @param comment Новый комментарий (может быть null)
     * @return [TransactionDTO] с обновлёнными данными транзакции
     */
    override suspend fun updateTransactionById(
        transactionId: Int,
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        comment: String?
    ): TransactionResponseDTO {
        return mapper.mapTransactionResponse(
            api.updateTransactionById(
                id = transactionId,
                request = mapper.mapTransactionToRequest(
                    accountId,
                    categoryId,
                    amount,
                    transactionDate,
                    comment
                )
            )
        )
    }

    /**
     * Удаляет транзакцию по её ID.
     *
     * @param transactionId ID транзакции, которую нужно удалить
     * @return Результат HTTP-запроса, содержащий статус выполнения
     */
    override suspend fun deleteTransactionById(transactionId: Int): Response<Void> {
        return api.deleteTransactionById(transactionId)
    }
}
