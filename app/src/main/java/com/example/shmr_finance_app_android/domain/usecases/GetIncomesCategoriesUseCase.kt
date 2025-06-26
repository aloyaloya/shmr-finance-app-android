package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.data.remote.api.NetworkChecker
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import dagger.Reusable
import javax.inject.Inject

// Честно говоря не совсем понятно какие статьи показывать, из запроса /categories
// или же из StatItem аккаунта, который мы получили.
// Зачем-то этот запрос есть, поэтому брал статьи из него
@Reusable
class GetIncomesCategoriesUseCase @Inject constructor(
    private val repository: CategoriesRepository,
    private val networkChecker: NetworkChecker
) {
    suspend operator fun invoke(): Result<List<CategoryDomain>> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.getCategoriesByType(true)
    }
}