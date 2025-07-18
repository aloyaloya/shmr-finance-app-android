package com.example.shmr_finance_app_android.presentation.feature.analytic.model

import com.example.shmr_finance_app_android.core.utils.formatWithSpaces
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

data class AnalyticResultModel(
    val categoryId: Int,
    val category: String,
    val percentage: String,
    val amount: String,
    val currency: String,
    val emoji: String
) {
    private val amountFormatted: String
        get() = "${amount.formatWithSpaces()} $currency"

    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent.Text(text = emoji),
            content = MainContent(title = category),
            trail = TrailContent(text = "$percentage%", subtext = amountFormatted)
        )
    }
}