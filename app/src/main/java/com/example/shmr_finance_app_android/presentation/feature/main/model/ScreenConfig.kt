package com.example.shmr_finance_app_android.presentation.feature.main.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ScreenConfig(
    val route: String,
    val topBarConfig: TopBarConfig,
    val floatingActionConfig: FloatingActionConfig? = null
)

data class TopBarConfig(
    @StringRes val titleResId: Int,
    val showBackButton: Boolean = false,
    val action: TopBarAction? = null
)

data class TopBarAction(
    @DrawableRes val iconResId: Int,
    @StringRes val descriptionResId: Int,
    val actionRoute: String
)

data class FloatingActionConfig(
    @StringRes val descriptionResId: Int,
    val actionRoute: String
)