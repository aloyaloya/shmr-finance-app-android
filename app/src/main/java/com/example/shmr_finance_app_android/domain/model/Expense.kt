package com.example.shmr_finance_app_android.domain.model

import com.example.shmr_finance_app_android.domain.model.ui.LeadContent
import com.example.shmr_finance_app_android.domain.model.ui.ListItem
import com.example.shmr_finance_app_android.domain.model.ui.MainContent
import com.example.shmr_finance_app_android.domain.model.ui.TrailContent

data class Expense(
    val id: String,
    val title: String,
    val amount: Int,
    val currency: String,
    val subtitle: String? = null,
    val emoji: String? = null
) {
    private val amountFormatted: String get() = "$amount $currency"

    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent(text = emoji?: generateTextFromTitle(title)),
            content = MainContent(title = title, subtitle = subtitle),
            trail = TrailContent(text = amountFormatted)
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