package com.example.shmr_finance_app_android.presentation.feature.categories.mapper

import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.presentation.feature.categories.model.IncomeCategoryUiModel
import javax.inject.Inject

class CategoryToIncomeCategoryMapper @Inject constructor() {
    fun map(domain: CategoryDomain): IncomeCategoryUiModel {
        return IncomeCategoryUiModel(
            id = domain.id,
            name = domain.name,
            emoji = domain.emoji
        )
    }
}