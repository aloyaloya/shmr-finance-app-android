package com.example.shmr_finance_app_android.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shmr_finance_app_android.R

sealed class RootScreen(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val label: Int
) {
    object Expenses : RootScreen(
        route = "payments_screen",
        icon = R.drawable.ic_expense,
        label = R.string.expense_screen_label
    )

    object Income : RootScreen(
        route = "income_screen",
        icon = R.drawable.ic_income,
        label = R.string.income_screen_label
    )

    object Balance : RootScreen(
        route = "balance_screen",
        icon = R.drawable.ic_balance,
        label = R.string.balance_screen_label
    )

    object Categories : RootScreen(
        route = "categories_screen",
        icon = R.drawable.ic_category,
        label = R.string.categories_screen_label
    )

    object Settings : RootScreen(
        route = "settings_screen",
        icon = R.drawable.ic_settings,
        label = R.string.settings_screen_label
    )

    companion object {
        val screens = listOf(Expenses, Income, Balance, Categories, Settings)
    }
}