package com.example.shmr_finance_app_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.shmr_finance_app_android.ui.screens.balance_screen.BalanceScreen
import com.example.shmr_finance_app_android.ui.screens.categories_screen.CategoriesScreen
import com.example.shmr_finance_app_android.ui.screens.expenses_screen.ExpensesScreen
import com.example.shmr_finance_app_android.ui.screens.income_screen.IncomeScreen
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarViewModel

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    topBarViewModel: TopBarViewModel,
    navController: NavHostController
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = RootScreen.Expenses.route
    ) {
        composable(route = RootScreen.Expenses.route) {
            ExpensesScreen(topBarViewModel)
        }
        composable(route = RootScreen.Income.route) {
            IncomeScreen(topBarViewModel)
        }
        composable(route = RootScreen.Balance.route) {
            BalanceScreen(topBarViewModel)
        }
        composable(route = RootScreen.Categories.route) {
            CategoriesScreen(topBarViewModel)
        }
        composable(route = RootScreen.Settings.route) {
            // Экран Настройки
        }
    }
}