package com.example.shmr_finance_app_android.data.datasource

import com.example.shmr_finance_app_android.data.model.AccountDTO
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.mapper.AccountRemoteMapper
import javax.inject.Inject

/**
 * Отвечает за получение данных аккаунта из remote-источника (API).
 * Определяет контракт для работы с данными аккаунта без привязки к конкретной реализации.
 */
interface AccountRemoteDataSource {
    suspend fun getAccountById(accountId: Int): AccountDTO
}

/**
 * Реализация [AccountRemoteDataSource], отвечающая за:
 * - Запрос данных аккаунта
 * - Преобразование сетевых моделей в DTO
 *
 * @param api - сервис для работы с API [FinanceApiService]
 * @param mapper - маппер для преобразования сетевых моделей в DTO [AccountRemoteDataSource]
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
     * @return [AccountDTO] - преобразованные DTO данные аккаунта
     */
    override suspend fun getAccountById(accountId: Int): AccountDTO {
        return mapper.mapAccount(api.getAccountById(accountId))
    }
}