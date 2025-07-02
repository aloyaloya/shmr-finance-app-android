package com.example.shmr_finance_app_android.data.remote.api

import com.example.shmr_finance_app_android.data.remote.model.AccountResponse
import com.example.shmr_finance_app_android.data.remote.model.AccountUpdateRequest
import com.example.shmr_finance_app_android.data.remote.model.CategoryResponse
import com.example.shmr_finance_app_android.data.remote.model.TransactionResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Отвечает за взаимодействие с API.
 * Методы возвращают сырые модели API (Response-классы), которые должны быть
 * преобразованы в доменные модели через мапперы.
 */
interface FinanceApiService {

    /**
     * Получает данные аккаунта по ID.
     * @param accountId - ID аккаунта
     * @return [AccountResponse] - модель ответа API
     */
    @GET("accounts/{id}")
    suspend fun getAccountById(@Path("id") accountId: Int): AccountResponse

    /**
     * Обновляет данные аккаунта по ID.
     * @param accountId ID аккаунта
     * @param request Тело запроса [AccountUpdateRequest]
     */
    @PUT("accounts/{id}")
    suspend fun updateAccountById(
        @Path("id") accountId: Int,
        @Body request: AccountUpdateRequest
    ): TransactionResponse

    /**
     * Получает список транзакций за период.
     * @param accountId - ID аккаунта
     * @param startDate - дата начала периода необходимых транзакций
     * (по умолчанию - начало текущего месяца)
     * @param endDate - дата конца периода необходимых транзакций
     * (по умолчанию - конец текущего месяца)
     * @return Список [TransactionResponse] или пустой список, если транзакций нет.
     */
    @GET("transactions/account/{accountId}/period")
    suspend fun getTransactionsByPeriod(
        @Path("accountId") accountId: Int,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null
    ): List<TransactionResponse>

    /**
     * Получает список категорий по типу (доход/расход).
     * @param isIncome - `true` для категорий доходов, `false` для расходов
     * @return Список [CategoryResponse] или пустой список, если статей нет
     */
    @GET("categories/type/{isIncome}")
    suspend fun getCategoriesByType(
        @Path("isIncome") isIncome: Boolean
    ): List<CategoryResponse>
}