package com.example.shmr_finance_app_android.data.model.domain

import com.example.shmr_finance_app_android.data.model.ui.ListItem
import com.example.shmr_finance_app_android.data.model.ui.MainContent
import com.example.shmr_finance_app_android.data.model.ui.TrailContent

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