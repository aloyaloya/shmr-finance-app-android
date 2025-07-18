package com.example.shmr_finance_app_android.data.datasources.account

import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.AccountDTO
import com.example.shmr_finance_app_android.data.model.AccountResponseDTO
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.mapper.AccountRemoteMapper
import javax.inject.Inject

/**
 * Реализация [AccountRemoteDataSource], отвечающая за работу с данным из remote-источника:
 * - Запрос данных аккаунта с сети
 * - Запроса изменения данных аккаунта
 * - Преобразование сетевых моделей в DTO
 *
 * @param api - сервис для работы с API [FinanceApiService]
 * @param mapper - маппер для преобразования сетевых моделей [AccountRemoteMapper]
 */
internal class AccountRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: AccountRemoteMapper
) : AccountRemoteDataSource {

    /**
     * Реализация получения данных аккаунта по ID:
     * 1. Запрашивает данные через API
     * 2. Преобразует результат в DTO
     * 3. Возвращает готовый DTO
     *
     * @param accountId - ID аккаунта
     * @return [AccountResponseDTO] - преобразованные DTO данные аккаунта
     */
    override suspend fun getAccountById(accountId: Int): AccountResponseDTO {
        return mapper.mapAccountResponse(api.getAccountById(accountId))
    }

    /**
     * Реализация изменения данных аккаунта по ID:
     * 1. Преобразует данные в AccountUpdateRequest
     * 3. Делает запрос для изменения данных через API
     *
     * @param [accountBrief] DTO данные аккаунта
     */
    override suspend fun updateAccountById(accountBrief: AccountBriefDTO): AccountDTO {
        return mapper.mapAccount(
            api.updateAccountById(
                accountId = accountBrief.id,
                request = mapper.mapAccountBrief(accountBrief)
            )
        )
    }
}