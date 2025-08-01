package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.core.network.AppError
import com.example.shmr_finance_app_android.core.network.NetworkChecker
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для получения всех категорий.
 * Поведение:
 * 1. Проверяет интернет-соединение через [NetworkChecker]
 * 2. Если сети нет - сразу возвращает [Result.failure] с [AppError.Network]
 * 3. Запрашивает категории через [CategoriesRepository.getAllCategories]
 */
@Reusable
class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository
) {
    /**
     * Получает список всех категорий.
     * @return [Result.success] с списком [CategoryDomain] или
     * [Result.failure] с [AppError.Network] если нет соединения
     */
    suspend operator fun invoke(): Result<List<CategoryDomain>> {
        return repository.getAllCategories()
    }
}