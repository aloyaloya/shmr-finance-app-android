package com.example.shmr_finance_app_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
            // Экран Расходы
        }
        composable(route = RootScreen.Income.route) {
            // Экран Доходы
        }
        composable(route = RootScreen.Balance.route) {
            // Экран Счет
        }
        composable(route = RootScreen.Categories.route) {
            // Экран Статьи
        }
        composable(route = RootScreen.Settings.route) {
            // Экран Настройки
        }
    }
}