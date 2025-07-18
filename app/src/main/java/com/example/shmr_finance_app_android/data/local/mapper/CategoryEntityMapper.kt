package com.example.shmr_finance_app_android.data.local.mapper

import com.example.shmr_finance_app_android.data.local.entities.CategoryEntity
import com.example.shmr_finance_app_android.data.model.CategoryDTO
import dagger.Reusable
import javax.inject.Inject

/**
 * Маппер для преобразования [CategoryEntity] -> [CategoryDTO]
 * Маппер для преобразования [CategoryDTO] -> [CategoryEntity]
 */
@Reusable
internal class CategoryEntityMapper @Inject constructor() {
    fun mapCategory(entity: CategoryEntity): CategoryDTO {
        return CategoryDTO(
            id = entity.id,
            name = entity.name,
            emoji = entity.emoji,
            isIncome = entity.isIncome
        )
    }

    fun mapCategoryToEntity(dto: CategoryDTO): CategoryEntity {
        return CategoryEntity(
            id = dto.id,
            name = dto.name,
            emoji = dto.emoji,
            isIncome = dto.isIncome
        )
    }
}