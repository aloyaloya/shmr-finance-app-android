package com.example.shmr_finance_app_android.core.navigation

/**
 * Отвечает за хранение и структурирование всех маршрутов (экранов) приложения.
 * Определяет иерархию навигации через sealed классы.
 */
sealed class Route(val path: String) {
    /**
     * Отвечает за хранение маршрутов корневых экранов,
     * которые доступны через BottomNavigation.
     */
    sealed class Root(path: String) : Route(path) {
        data object Expenses : Root(path = "expenses_screen")
        data object Income : Root(path = "income_screen")
        data object Balance : Root(path = "balance_screen")
        data object Categories : Root(path = "categories_screen")
        data object Settings : Root(path = "settings_screen")
    }

    /**
     * Отвечает за хранение маршрутов дочерних экранов,
     * которые требуют передачи параметров или не отображаются в BottomNavigation.
     */
    sealed class SubScreens(path: String) : Route(path) {
        data object ExpensesHistory : Root(path = "history_screen/expenses")
        data object IncomesHistory : Root(path = "history_screen/incomes")

        data object BalanceEdit : SubScreens("balance_edit/{balanceId}") {
            fun balanceId(): String = "balanceId"
            fun route(balanceId: Int) = "balance_edit/$balanceId"
        }
    }
}