package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.FinanceRepository
import javax.inject.Inject

class GetIncomesCategoriesUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(): Result<List<CategoryDomain>> {
        return try {
            Result.success(repository.getCategoriesByType(true))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}