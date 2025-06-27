package com.example.shmr_finance_app_android.data.datasource

import com.example.shmr_finance_app_android.data.model.CategoryDTO
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.remote.mapper.CategoryRemoteMapper
import javax.inject.Inject

/**
 * Отвечает за получение данных аккаунта из remote-источника (API).
 * Определяет контракт для работы с данными категорий без привязки к конкретной реализации.
 */
interface CategoriesRemoteDataSource {
    suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDTO>
}

/**
 * Реализация [CategoriesRemoteDataSource], отвечающая за:
 * - Запрос данных категорий
 * - Преобразование сетевых моделей в DTO
 *
 * @param api - сервис для работы с API [FinanceApiService]
 * @param mapper - маппер для преобразования сетевых моделей в DTO [CategoryRemoteMapper]
 */
internal class CategoriesRemoteDataSourceImpl @Inject constructor(
    private val api: FinanceApiService,
    private val mapper: CategoryRemoteMapper
) : CategoriesRemoteDataSource {

    /**
     * Реализация получения данных категорий по типу:
     * 1. Запрашивает данные через API
     * 2. Преобразует результат в DTO
     * 3. Возвращает готовый список DTO
     *
     * @param isIncome - тип категорий для получения
     * @return Список [CategoryDTO] - список преобразованных DTO данных категорий
     */
    override suspend fun getCategoriesByType(isIncome: Boolean): List<CategoryDTO> {
        return api.getCategoriesByType(isIncome)
            .map(mapper::mapCategory)
    }
}