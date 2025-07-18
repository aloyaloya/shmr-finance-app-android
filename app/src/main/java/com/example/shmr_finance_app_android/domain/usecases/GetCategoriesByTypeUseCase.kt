package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.core.network.AppError
import com.example.shmr_finance_app_android.core.network.NetworkChecker
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для получения категорий по типу доход/расход.
 * Поведение:
 * 1. Проверяет интернет-соединение через [NetworkChecker]
 * 2. Если сети нет - сразу возвращает [Result.failure] с [AppError.Network]
 * 3. Запрашивает категории через [CategoriesRepository.getCategoriesByType]
 */
@Reusable
class GetCategoriesByTypeUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    /**
     * Получает список категорий доходов.
     * @return [Result.success] с списком [CategoryDomain] или
     * [Result.failure] с [AppError.Network] если нет соединения
     */
    suspend operator fun invoke(isIncome: Boolean): Result<List<CategoryDomain>> {
        return repository.getCategoriesByType(isIncome)
    }
}