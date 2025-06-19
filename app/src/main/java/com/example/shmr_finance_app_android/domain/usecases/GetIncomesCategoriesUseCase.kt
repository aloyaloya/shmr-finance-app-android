package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.repository.FinanceRepository
import dagger.Reusable
import javax.inject.Inject

// Честно говоря не совсем понятно какие статьи показывать, из запроса /categories
// или же из StatItem аккаунта, который мы получили.
// Зачем-то этот запрос есть, поэтому брал статьи из него
@Reusable
class GetIncomesCategoriesUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(): List<CategoryDomain> {
        return repository.getCategoriesByType(true)
    }
}