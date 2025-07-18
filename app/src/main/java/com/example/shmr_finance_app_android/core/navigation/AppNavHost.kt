package com.example.shmr_finance_app_android.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shmr_finance_app_android.presentation.feature.balance.navigation.BalanceNavigation
import com.example.shmr_finance_app_android.presentation.feature.categories.screen.CategoriesScreen
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.settings.screen.SettingsScreen
import com.example.shmr_finance_app_android.presentation.feature.transaction.navigation.ExpensesNavigation
import com.example.shmr_finance_app_android.presentation.feature.transaction.navigation.IncomesNavigation

/**
 * Отвечает за навигацию между экранами приложения:
 * - Управляет переходами между корневыми
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
        startDestination = Route.Root.ExpenseNavigation.path
    ) {

        /**
         * Отвечает за:
         * 1. Отображение экранов Расходов (вложенный граф)
         * 2. Передачу конфигурации топ-бара
         */
        composable(route = Route.Root.ExpenseNavigation.path) {
            ExpensesNavigation(updateConfigState = updateConfigState)
        }

        /**
         * Отвечает за:
         * 1. Отображение экранов Доходов (вложенный граф)
         * 2. Передачу конфигурации топ-бара
         */
        composable(route = Route.Root.IncomeNavigation.path) {
            IncomesNavigation(updateConfigState = updateConfigState)
        }

        /**
         * Отвечает за:
         * 1. Отображение экранов Баланса (вложенный граф)
         * 2. Передачу конфигурации топ-бара
         */
        composable(route = Route.Root.BalanceNavigation.path) {
            BalanceNavigation(updateConfigState = updateConfigState)
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
    }
}