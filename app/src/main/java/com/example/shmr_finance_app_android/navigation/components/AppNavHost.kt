package com.example.shmr_finance_app_android.navigation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shmr_finance_app_android.navigation.config.ScreenConfig
import com.example.shmr_finance_app_android.navigation.routes.Route
import com.example.shmr_finance_app_android.ui.screens.balance_screen.BalanceScreen
import com.example.shmr_finance_app_android.ui.screens.categories_screen.CategoriesScreen
import com.example.shmr_finance_app_android.ui.screens.expenses_history_screen.ExpensesHistoryScreen
import com.example.shmr_finance_app_android.ui.screens.expenses_screen.ExpensesScreen
import com.example.shmr_finance_app_android.ui.screens.income_screen.IncomeScreen
import com.example.shmr_finance_app_android.ui.screens.settings_screen.SettingsScreen

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
        composable(route = Route.Root.Expenses.path) {
            ExpensesScreen(updateConfigState = updateConfigState)
        }
        composable(route = Route.Root.Income.path) {
            IncomeScreen(updateConfigState = updateConfigState)
        }
        composable(route = Route.Root.Balance.path) {
            BalanceScreen(updateConfigState = updateConfigState)
        }
        composable(route = Route.Root.Categories.path) {
            CategoriesScreen(updateConfigState = updateConfigState)
        }
        composable(route = Route.Root.Settings.path) {
            SettingsScreen(updateConfigState = updateConfigState)
        }
        composable(route = Route.ExpensesSubScreens.ExpensesHistory.path) {
            ExpensesHistoryScreen(updateConfigState = updateConfigState)
        }
    }
}