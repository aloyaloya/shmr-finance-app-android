package com.example.shmr_finance_app_android.presentation.feature.transaction.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.history.screen.HistoryScreen
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.transaction.screens.TransactionCreationScreen
import com.example.shmr_finance_app_android.presentation.feature.transaction.screens.TransactionUpdateScreen
import com.example.shmr_finance_app_android.presentation.feature.transaction.screens.TransactionsTodayScreen

@Composable
fun IncomesNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.IncomesScreens.TransactionsToday.path
    ) {
        /**
         * Отвечает за:
         * 1. Отображение экрана Доходы сегодня
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на экран Истории доходов
         * 4. Обработку навигации на экран Редактирование транзакции дохода
         * 5. Обработку навигации на экран Создания транзакции дохода
         */
        composable(route = Route.IncomesScreens.TransactionsToday.path) {
            TransactionsTodayScreen(
                updateConfigState = updateConfigState,
                isIncome = true,
                onHistoryNavigate = {
                    navController.navigate(Route.IncomesScreens.History.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCreateNavigate = {
                    navController.navigate(Route.IncomesScreens.TransactionCreation.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onTransactionUpdateNavigate = {
                    navController.navigate(Route.IncomesScreens.TransactionUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана Создание транзакции дохода
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на предыдущий экран
         */
        composable(route = Route.IncomesScreens.TransactionCreation.path) {
            TransactionCreationScreen(
                isIncome = true,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана редактирования транзакции дохода
         * 2. Обработку аргумента ID счета [transactionId]
         * 3. Передачу конфигурации топ-бара
         * 4. Обработку навигации на предыдущий экран
         */
        composable(
            route = Route.IncomesScreens.TransactionUpdate.path,
            arguments = listOf(navArgument(Route.IncomesScreens.TransactionUpdate.transactionId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString(
                Route.IncomesScreens.TransactionUpdate.transactionId()
            ) ?: ""
            TransactionUpdateScreen(
                transactionId = transactionId,
                isIncome = true,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана История доходов
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на предыдущий экран
         * 4. Обработку навигации на экран Редактирование транзакции дохода
         */
        composable(route = Route.IncomesScreens.History.path) {
            HistoryScreen(
                isIncome = true,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() },
                onTransactionUpdateNavigate = {
                    navController.navigate(Route.IncomesScreens.TransactionUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}