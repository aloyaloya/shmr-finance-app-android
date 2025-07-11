package com.example.shmr_finance_app_android.data.remote.mapper

import com.example.shmr_finance_app_android.data.model.CategoryDTO
import com.example.shmr_finance_app_android.data.remote.model.Category
import dagger.Reusable
import javax.inject.Inject

/**
 * Маппер для преобразования [Category] -> [CategoryDTO]
 */
@Reusable
internal class CategoryRemoteMapper @Inject constructor() {
    fun mapCategory(response: Category): CategoryDTO {
        return CategoryDTO(
            id = response.id,
            name = response.name,
            emoji = response.emoji,
            isIncome = response.isIncome,
        )
    }
}