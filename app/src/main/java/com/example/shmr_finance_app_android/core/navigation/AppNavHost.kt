package com.example.shmr_finance_app_android.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.shmr_finance_app_android.presentation.feature.balance.screen.BalanceScreen
import com.example.shmr_finance_app_android.presentation.feature.balance_update.screen.BalanceUpdateScreen
import com.example.shmr_finance_app_android.presentation.feature.categories.screen.CategoriesScreen
import com.example.shmr_finance_app_android.presentation.feature.expenses.screen.ExpensesScreen
import com.example.shmr_finance_app_android.presentation.feature.history.screen.HistoryScreen
import com.example.shmr_finance_app_android.presentation.feature.incomes.screen.IncomeScreen
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.settings.screen.SettingsScreen
import com.example.shmr_finance_app_android.presentation.feature.transaction_creation.screen.TransactionCreationScreen
import com.example.shmr_finance_app_android.presentation.feature.transaction_update.screen.TransactionUpdateScreen

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
         * Отвечает за:
         * 1. Отображение экрана Расходы
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на экран Истории расходов
         * 4. Обработку навигации на экран Редактирование расхода
         */
        composable(route = Route.Root.Expenses.path) {
            ExpensesScreen(
                updateConfigState = updateConfigState,
                onHistoryNavigate = {
                    navController.navigate(Route.SubScreens.ExpensesHistory.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCreateNavigate = {
                    navController.navigate(Route.SubScreens.ExpenseTransactionCreation.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onTransactionUpdateNavigate = {
                    navController.navigate(Route.SubScreens.ExpenseTransactionUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана Доходы
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на экран Истории доходов
         * 4. Обработку навигации на экран Редактирование дохода
         */
        composable(route = Route.Root.Income.path) {
            IncomeScreen(
                updateConfigState = updateConfigState,
                onHistoryNavigate = {
                    navController.navigate(Route.SubScreens.IncomesHistory.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onCreateNavigate = {
                    navController.navigate(Route.SubScreens.IncomeTransactionCreation.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onTransactionUpdateNavigate = {
                    navController.navigate(Route.SubScreens.IncomeTransactionUpdate.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана Cчет
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на экран Редактирования счета
         */
        composable(route = Route.Root.Balance.path) {
            BalanceScreen(
                updateConfigState = updateConfigState,
                onEditNavigate = {
                    navController.navigate(Route.SubScreens.BalanceEdit.route(it)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
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
         * 1. Отображение экрана История расходов
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на предыдущий экран
         */
        composable(route = Route.SubScreens.ExpensesHistory.path) {
            HistoryScreen(
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана История доходов
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на предыдущий экран
         */
        composable(route = Route.SubScreens.IncomesHistory.path) {
            HistoryScreen(
                isIncome = true,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана Создание транзакции расхода
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на предыдущий экран
         */
        composable(route = Route.SubScreens.ExpenseTransactionCreation.path) {
            TransactionCreationScreen(
                isIncome = false,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }

        /**
         * Отвечает за:
         * 1. Отображение экрана Создание транзакции дохода
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на предыдущий экран
         */
        composable(route = Route.SubScreens.IncomeTransactionCreation.path) {
            TransactionCreationScreen(
                isIncome = true,
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
            route = Route.SubScreens.ExpenseTransactionUpdate.path,
            arguments = listOf(navArgument(Route.SubScreens.ExpenseTransactionUpdate.transactionId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString(
                Route.SubScreens.ExpenseTransactionUpdate.transactionId()
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
         * 1. Отображение экрана редактирования транзакции дохода
         * 2. Обработку аргумента ID счета [transactionId]
         * 3. Передачу конфигурации топ-бара
         * 4. Обработку навигации на предыдущий экран
         */
        composable(
            route = Route.SubScreens.IncomeTransactionUpdate.path,
            arguments = listOf(navArgument(Route.SubScreens.IncomeTransactionUpdate.transactionId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val transactionId = backStackEntry.arguments?.getString(
                Route.SubScreens.IncomeTransactionUpdate.transactionId()
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
         * - Отображение экрана Редактирование счета
         * - Обработку аргумента ID счета [balanceId]
         * - Передачу конфигурации топ-бара
         */
        composable(
            route = Route.SubScreens.BalanceEdit.path,
            arguments = listOf(navArgument(Route.SubScreens.BalanceEdit.balanceId()) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val balanceId = backStackEntry.arguments?.getString(
                Route.SubScreens.BalanceEdit.balanceId()
            ) ?: ""
            BalanceUpdateScreen(
                balanceId = balanceId,
                updateConfigState = updateConfigState,
                onBackNavigate = { navController.popBackStack() }
            )
        }
    }
}