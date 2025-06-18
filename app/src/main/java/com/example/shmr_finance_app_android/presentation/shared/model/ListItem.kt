package com.example.shmr_finance_app_android.presentation.shared.model

import androidx.compose.ui.graphics.Color

data class ListItem(
    val lead: LeadContent? = null,
    val content: MainContent,
    val trail: TrailContent? = null
)

data class LeadContent(
    val text: String,
    val color: Color? = null
)

data class MainContent(
    val title: String,
    val subtitle: String? = null
)

data class TrailContent(
    val text: String,
    val subtext: String? = null
)