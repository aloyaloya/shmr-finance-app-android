package com.example.shmr_finance_app_android.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.shmr_finance_app_android.R

/**
 * Отвечает за представление элемента нижней панели навигации:
 * @property labelResId - название экрана для отображение на панели, из ресурсов
 * @property iconResId - иконка из ресурсов
 * @property route - ссылка на маршрут экрана
 */
data class BottomBarItem(
    @StringRes val labelResId: Int,
    @DrawableRes val iconResId: Int,
    val route: Route.Root
) {
    /**
     * Отвечает за хранение и предоставление всех элементов нижней панели навигации.
     */
    companion object {
        val items = listOf(
            BottomBarItem(
                labelResId = R.string.expense_screen_label,
                iconResId = R.drawable.ic_expense,
                route = Route.Root.ExpenseNavigation
            ),
            BottomBarItem(
                labelResId = R.string.income_screen_label,
                iconResId = R.drawable.ic_income,
                route = Route.Root.IncomeNavigation
            ),
            BottomBarItem(
                labelResId = R.string.balance_screen_label,
                iconResId = R.drawable.ic_balance,
                route = Route.Root.BalanceNavigation
            ),
            BottomBarItem(
                labelResId = R.string.categories_screen_label,
                iconResId = R.drawable.ic_category,
                route = Route.Root.Categories
            ),
            BottomBarItem(
                labelResId = R.string.settings_screen_label,
                iconResId = R.drawable.ic_settings,
                route = Route.Root.SettingsNavigation
            )
        )
    }
}