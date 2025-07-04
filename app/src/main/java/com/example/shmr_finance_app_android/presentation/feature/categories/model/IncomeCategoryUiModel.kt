package com.example.shmr_finance_app_android.presentation.feature.categories.model

import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent

data class IncomeCategoryUiModel(
    val id: Int,
    val name: String,
    val emoji: String
) {
    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent.Text(text = emoji),
            content = MainContent(title = name)
        )
    }
}
