package com.example.shmr_finance_app_android.data.mapper

import com.example.shmr_finance_app_android.data.model.domain.Expense
import com.example.shmr_finance_app_android.data.model.ui.LeadContent
import com.example.shmr_finance_app_android.data.model.ui.ListItem
import com.example.shmr_finance_app_android.data.model.ui.MainContent
import com.example.shmr_finance_app_android.data.model.ui.TrailContent

fun Expense.toListItem(): ListItem {
    return ListItem(
        lead = createLeadContent(this),
        content = MainContent(
            title = this.title,
            subtitle = this.author
        ),
        trail = TrailContent(
            text = this.amountFormatted
        )
    )
}

private fun createLeadContent(payment: Expense): LeadContent {
    return if (payment.emoji != null) {
        LeadContent(text = payment.emoji)
    } else {
        val generatedText = generateTextFromTitle(payment.title)
        LeadContent(text = generatedText)
    }
}

private fun generateTextFromTitle(title: String): String {
    return title.split(" ")
        .take(2)
        .mapNotNull { word -> word.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .take(2)
}