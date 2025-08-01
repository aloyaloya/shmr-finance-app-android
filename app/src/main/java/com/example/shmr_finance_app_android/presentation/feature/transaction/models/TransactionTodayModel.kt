package com.example.shmr_finance_app_android.presentation.feature.transaction.models

import com.example.shmr_finance_app_android.core.utils.formatWithSpaces
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

data class TransactionTodayModel(
    val id: Int,
    val title: String,
    val subtitle: String?,
    val currency: String,
    val amount: String,
    val emoji: String
) {
    private val amountFormatted: String
        get() = "${amount.formatWithSpaces()} $currency"

    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent.Text(text = emoji),
            content = MainContent(title = title, subtitle = subtitle),
            trail = TrailContent(text = amountFormatted)
        )
    }
}