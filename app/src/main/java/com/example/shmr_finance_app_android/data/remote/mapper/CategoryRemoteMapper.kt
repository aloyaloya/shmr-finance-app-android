package com.example.shmr_finance_app_android.data.remote.mapper

import com.example.shmr_finance_app_android.data.model.CategoryDTO
import com.example.shmr_finance_app_android.data.remote.model.CategoryResponse
import dagger.Reusable
import javax.inject.Inject

@Reusable
class CategoryRemoteMapper @Inject constructor() {
    fun mapCategory(response: CategoryResponse): CategoryDTO {
        return CategoryDTO(
            id = response.id,
            name = response.name,
            emoji = response.emoji,
            isIncome = response.isIncome,
        )
    }
}