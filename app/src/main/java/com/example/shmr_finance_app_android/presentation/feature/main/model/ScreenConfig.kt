package com.example.shmr_finance_app_android.presentation.feature.main.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ScreenConfig(
    val topBarConfig: TopBarConfig,
    val floatingActionConfig: FloatingActionConfig? = null
)

data class TopBarConfig(
    @StringRes val titleResId: Int,
    val backAction: TopBarBackAction? = null,
    val action: TopBarAction? = null
)

data class TopBarAction(
    @DrawableRes val iconResId: Int,
    @StringRes val descriptionResId: Int,
    val isActive: () -> Boolean = { true },
    val actionUnit: () -> Unit
)

data class TopBarBackAction(
    @DrawableRes val iconResId: Int? = null,
    @StringRes val descriptionResId: Int? = null,
    val actionUnit: () -> Unit
)

data class FloatingActionConfig(
    @StringRes val descriptionResId: Int,
    val actionUnit: () -> Unit
)