package com.example.shmr_finance_app_android.domain.model

import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

data class Income(
    val id: String,
    val title: String,
    val amount: Int,
    val currency: String
) {
    private val amountFormatted: String get() = "$amount $currency"

    fun toListItem(): ListItem {
        return ListItem(
            content = MainContent(title = title),
            trail = TrailContent(text = amountFormatted)
        )
    }
}