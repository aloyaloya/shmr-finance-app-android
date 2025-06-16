package com.example.shmr_finance_app_android.navigation.routes

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shmr_finance_app_android.R

sealed class Route(val path: String) {
    sealed class Root(path: String) : Route(path) {
        data object Expenses :  Root(path = "expenses_screen")
        data object Income : Root(path = "income_screen")
        data object Balance : Root(path = "balance_screen")
        data object Categories : Root(path = "categories_screen")
        data object Settings : Root(path = "settings_screen")
    }
}

data class BottomBarItem(
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int,
    val route: Route.Root
) {
    companion object {
        val items = listOf(
            BottomBarItem(
                labelResId = R.string.expense_screen_label,
                iconResId = R.drawable.ic_expense,
                route = Route.Root.Expenses
            ),
            BottomBarItem(
                labelResId = R.string.income_screen_label,
                iconResId = R.drawable.ic_income,
                route = Route.Root.Income
            ),
            BottomBarItem(
                labelResId = R.string.balance_screen_label,
                iconResId = R.drawable.ic_balance,
                route = Route.Root.Balance
            ),
            BottomBarItem(
                labelResId = R.string.categories_screen_label,
                iconResId = R.drawable.ic_category,
                route = Route.Root.Categories
            ),
            BottomBarItem(
                labelResId = R.string.settings_screen_label,
                iconResId = R.drawable.ic_settings,
                route = Route.Root.Settings
            )
        )
    }
}