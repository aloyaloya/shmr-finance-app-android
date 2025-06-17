package com.example.shmr_finance_app_android.domain.model

import com.example.shmr_finance_app_android.domain.model.ui.LeadContent
import com.example.shmr_finance_app_android.domain.model.ui.ListItem
import com.example.shmr_finance_app_android.domain.model.ui.MainContent

data class Category(
    val id: String,
    val title: String,
    val emoji: String? = null
) {
    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent(text = emoji?: generateTextFromTitle(title)),
            content = MainContent(title = title)
        )
    }

    private fun generateTextFromTitle(title: String): String {
        return title.split(" ")
            .take(2)
            .mapNotNull { word -> word.firstOrNull()?.uppercaseChar() }
            .joinToString("")
            .take(2)
    }
}