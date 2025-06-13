package com.example.shmr_finance_app_android.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shmr_finance_app_android.R

sealed class RootScreen(
    val route: String,
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    @StringRes val title: Int,
    val showBackButton: Boolean = false,
    @DrawableRes val actionIcon: Int? = null,
    @StringRes val actionDescription: Int? = null,
    val onActionRoute: String? = null,
    val onFloatingActionRoute: String? = null,
    @StringRes val floatingActionDescription: Int? = null
) {
    object Expenses : RootScreen(
        route = "payments_screen",
        icon = R.drawable.ic_expense,
        label = R.string.expense_screen_label,
        title = R.string.expense_screen_title,
        actionIcon = R.drawable.ic_history,
        actionDescription = R.string.expenses_history_description,
        onActionRoute = "",
        onFloatingActionRoute = "",
        floatingActionDescription = R.string.add_expense_description
    )

    object Income : RootScreen(
        route = "income_screen",
        icon = R.drawable.ic_income,
        label = R.string.income_screen_label,
        title = R.string.income_screen_title,
        actionIcon = R.drawable.ic_history,
        actionDescription = R.string.incomes_history_description,
        onActionRoute = "",
        onFloatingActionRoute = "",
        floatingActionDescription = R.string.add_income_description
    )

    object Balance : RootScreen(
        route = "balance_screen",
        icon = R.drawable.ic_balance,
        label = R.string.balance_screen_label,
        title = R.string.balance_screen_title,
        actionIcon = R.drawable.ic_history,
        actionDescription = R.string.balance_edit_description,
        onActionRoute = ""
    )

    object Categories : RootScreen(
        route = "categories_screen",
        icon = R.drawable.ic_category,
        label = R.string.categories_screen_label,
        title = R.string.categories_screen_title
    )

    object Settings : RootScreen(
        route = "settings_screen",
        icon = R.drawable.ic_settings,
        label = R.string.settings_screen_label,
        title = R.string.settings_screen_title
    )

    companion object {
        val screens = listOf(Expenses, Income, Balance, Categories, Settings)

        fun fromRoute(route: String?): RootScreen? {
            return screens.firstOrNull { it.route == route }
        }
    }
}