package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.data.remote.api.NetworkChecker
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для получения категорий доходов.
 * Поведение:
 * 1. Проверяет интернет-соединение через [NetworkChecker]
 * 2. Если сети нет - сразу возвращает [Result.failure] с [AppError.Network]
 * 3. Запрашивает категории через [CategoriesRepository.getAllCategories]
 *
 *  * Честно говоря не совсем понятно какие статьи показывать, из запроса /categories
 *  * или же из StatItem аккаунта, который мы получили.
 *  * Зачем-то этот запрос есть, поэтому брал статьи из него
 */
@Reusable
class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    private val networkChecker: NetworkChecker
) {
    /**
     * Получает список всех категорий.
     * @return [Result.success] с списком [CategoryDomain] или
     * [Result.failure] с [AppError.Network] если нет соединения
     */
    suspend operator fun invoke(): Result<List<CategoryDomain>> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.getAllCategories()
    }
}