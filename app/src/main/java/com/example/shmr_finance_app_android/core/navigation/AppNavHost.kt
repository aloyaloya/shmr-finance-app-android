package com.example.shmr_finance_app_android.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shmr_finance_app_android.presentation.feature.balance.screen.BalanceScreen
import com.example.shmr_finance_app_android.presentation.feature.categories.screen.CategoriesScreen
import com.example.shmr_finance_app_android.presentation.feature.expenses.screen.ExpensesScreen
import com.example.shmr_finance_app_android.presentation.feature.history.screen.HistoryScreen
import com.example.shmr_finance_app_android.presentation.feature.incomes.screen.IncomeScreen
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.settings.screen.SettingsScreen

/**
 * Отвечает за навигацию между экранами приложения:
 * - Определяет граф навигации со всеми возможными маршрутами
 * - Управляет переходами между корневыми и дочерними экранами
 * - Обрабатывает передачу аргументов между экранами
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    updateConfigState: (ScreenConfig) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Root.Expenses.path
    ) {
        /**
         * Отвечает за отображение экрана Расходы и передачу конфигурации топ-бара
         */
        composable(route = Route.Root.Expenses.path) {
            ExpensesScreen(updateConfigState = updateConfigState)
        }

        /**
         * Отвечает за отображение экрана Доходы и передачу конфигурации топ-бара
         */
        composable(route = Route.Root.Income.path) {
            IncomeScreen(updateConfigState = updateConfigState)
        }

        /**
         * Отвечает за отображение экрана Счет и передачу конфигурации топ-бара
         */
        composable(route = Route.Root.Balance.path) {
            BalanceScreen(updateConfigState = updateConfigState)
        }

        /**
         * Отвечает за отображение экрана Статьи и передачу конфигурации топ-бара
         */
        composable(route = Route.Root.Categories.path) {
            CategoriesScreen(updateConfigState = updateConfigState)
        }

        /**
         * Отвечает за отображение экрана Настройки и передачу конфигурации топ-бара
         */
        composable(route = Route.Root.Settings.path) {
            SettingsScreen(updateConfigState = updateConfigState)
        }

        /**
         * Отвечает за:
         * - Отображение экрана История
         * - Обработку аргумента типа операции (доходы/расходы) [isIncome]
         * - Передачу конфигурации топ-бара
         */
        composable(
            route = Route.SubScreens.History.path,
            arguments = listOf(navArgument(Route.SubScreens.History.isIncome()) {
                type = NavType.BoolType
            })
        ) { backStackEntry ->
            val isIncome = backStackEntry.arguments?.getBoolean(
                Route.SubScreens.History.isIncome()
            ) ?: false
            HistoryScreen(
                isIncome = isIncome,
                updateConfigState = updateConfigState
            )
        }
    }
}