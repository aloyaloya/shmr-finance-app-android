package com.example.shmr_finance_app_android.presentation.feature.categories.mapper

import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.presentation.feature.categories.model.CategoryUiModel
import javax.inject.Inject

/**
 * Маппер для преобразования [CategoryDomain] -> [CategoryUiModel]
 */
class CategoryToCategoryUiMapper @Inject constructor() {
    fun map(domain: CategoryDomain): CategoryUiModel {
        return CategoryUiModel(
            id = domain.id,
            name = domain.name,
            emoji = domain.emoji
        )
    }
}