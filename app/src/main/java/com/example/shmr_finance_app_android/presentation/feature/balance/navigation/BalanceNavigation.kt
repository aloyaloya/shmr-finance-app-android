package com.example.shmr_finance_app_android.presentation.feature.balance.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.balance.screens.BalanceScreen
import com.example.shmr_finance_app_android.presentation.feature.balance.screens.BalanceUpdateScreen
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig

@Composable
fun BalanceNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.BalanceScreens.Balance.path
    ) {

        /**
         * Отвечает за:
         * 1. Отображение экрана Cчет
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на экран Редактирования счета
         */
        composable(route = Route.BalanceScreens.Balance.path) {
            BalanceScreen(
                updateConfigState = updateConfigState,
                onEditNavigate = {
                    navController.navigate(Route.BalanceScreens.BalanceUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        /**
         * Отвечает за:
         * - Отображение экрана Редактирование счета
         * - Обработку аргумента ID счета [balanceId]
         * - Передачу конфигурации топ-бара
         */
        composable(
            route = Route.BalanceScreens.BalanceUpdate.path,
            arguments = listOf(navArgument(Route.BalanceScreens.BalanceUpdate.balanceId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val balanceId = backStackEntry.arguments?.getString(
                Route.BalanceScreens.BalanceUpdate.balanceId()
            ) ?: ""
            BalanceUpdateScreen(
                balanceId = balanceId,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }
    }
}