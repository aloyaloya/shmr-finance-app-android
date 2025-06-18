package com.example.shmr_finance_app_android.domain.model

import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent

data class CategoryDomain(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
) {
    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent(text = emoji),
            content = MainContent(title = name)
        )
    }
}
