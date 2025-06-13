package com.example.shmr_finance_app_android.data.model.ui

import androidx.compose.ui.graphics.Color

data class ListItem(
    val lead: LeadContent?,
    val content: MainContent,
    val trail: TrailContent?
)

data class LeadContent(
    val text: String? = null,
    val color: Color? = null
)

data class MainContent(
    val title: String,
    val subtitle: String? = null
)

data class TrailContent(
    val text: String
)