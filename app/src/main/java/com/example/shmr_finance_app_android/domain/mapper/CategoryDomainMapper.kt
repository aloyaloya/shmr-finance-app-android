package com.example.shmr_finance_app_android.domain.mapper

import com.example.shmr_finance_app_android.data.model.CategoryDTO
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import javax.inject.Inject

class CategoryDomainMapper @Inject constructor() {
    fun mapCategory(dto: CategoryDTO): CategoryDomain {
        return CategoryDomain(
            id = dto.id,
            name = dto.name,
            emoji = dto.emoji,
            isIncome = dto.isIncome
        )
    }
}