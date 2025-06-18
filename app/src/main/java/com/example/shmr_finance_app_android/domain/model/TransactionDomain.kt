package com.example.shmr_finance_app_android.domain.model

import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent
import com.example.shmr_finance_app_android.core.utils.formatWithSpaces
import java.time.LocalDate
import java.time.LocalTime

data class TransactionDomain(
    val id: Int,
    val account: AccountBriefDomain,
    val category: CategoryDomain,
    val amount: Int,
    val transactionDate: LocalDate,
    val transactionTime: LocalTime,
    val comment: String?,
) {
    private val amountFormatted: String
        get() = "${amount.toString().formatWithSpaces()} ${account.getCurrencySymbol()}"

    fun toListItem(): ListItem {
        return ListItem(
            lead = LeadContent(text = category.emoji),
            content = MainContent(title = category.name, subtitle = comment),
            trail = TrailContent(text = amountFormatted)
        )
    }
}