package com.example.shmr_finance_app_android.data.mapper

import com.example.shmr_finance_app_android.data.model.domain.Income
import com.example.shmr_finance_app_android.data.model.ui.ListItem
import com.example.shmr_finance_app_android.data.model.ui.MainContent
import com.example.shmr_finance_app_android.data.model.ui.TrailContent

fun Income.toListItem(): ListItem {
    return ListItem(
        lead = null,
        content = MainContent(
            title = this.title
        ),
        trail = TrailContent(
            text = this.amountFormatted
        )
    )
}