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
fun ExpensesNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.ExpensesScreens.TransactionsToday.path
    ) {
        /**
         * Отвечает за:
         * 1. Отображение экрана Расходы сегодня
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на экран Истории расходов
         * 4. Обработку навигации на экран Редактирование транзакции расхода
         * 5. Обработку навигации на экран Создания транзакции расхода
         */
        composable(route = Route.ExpensesScreens.TransactionsToday.path) {
            TransactionsTodayScreen(
                updateConfigState = updateConfigState,
                isIncome = false,
                onHistoryNavigate = {
                    navController.navigate(Route.ExpensesScreens.History.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCreateNavigate = {
                    navController.navigate(Route.ExpensesScreens.TransactionCreation.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onTransactionUpdateNavigate = {
                    navController.navigate(Route.ExpensesScreens.TransactionUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана Создание транзакции расхода
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на предыдущий экран
         */
        composable(route = Route.ExpensesScreens.TransactionCreation.path) {
            TransactionCreationScreen(
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана редактирования транзакции расхода
         * 2. Обработку аргумента ID счета [transactionId]
         * 3. Передачу конфигурации топ-бара
         * 4. Обработку навигации на предыдущий экран
         */
        composable(
            route = Route.ExpensesScreens.TransactionUpdate.path,
            arguments = listOf(navArgument(Route.ExpensesScreens.TransactionUpdate.transactionId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString(
                Route.ExpensesScreens.TransactionUpdate.transactionId()
            ) ?: ""
            TransactionUpdateScreen(
                transactionId = transactionId,
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана История расходов
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на предыдущий экран
         * 4. Обработку навигации на экран Редактирование транзакции расхода
         */
        composable(route = Route.ExpensesScreens.History.path) {
            HistoryScreen(
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() },
                onTransactionUpdateNavigate = {
                    navController.navigate(Route.ExpensesScreens.TransactionUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}