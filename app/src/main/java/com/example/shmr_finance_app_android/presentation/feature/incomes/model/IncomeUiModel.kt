package com.example.shmr_finance_app_android.presentation.feature.incomes.model

import com.example.shmr_finance_app_android.core.utils.formatWithSpaces
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

data class IncomeUiModel(
    val id: Int,
    val title: String,
    val amount: Int,
    val currency: String,
    val subtitle: String?,
    val emoji: String
) {
    private val amountFormatted: String
        get() = "${amount.toString().formatWithSpaces()} $currency"

    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent(text = emoji),
            content = MainContent(title = title, subtitle = subtitle),
            trail = TrailContent(text = amountFormatted)
        )
    }
}
