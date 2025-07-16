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
        data object ExpenseNavigation : Root(path = "expenses_navigation")
        data object IncomeNavigation : Root(path = "incomes_navigation")
        data object BalanceNavigation : Root(path = "balance_navigation")
        data object Categories : Root(path = "categories_screen")
        data object Settings : Root(path = "settings_screen")
    }

    sealed class IncomesScreens(path: String) : Route(path) {
        data object TransactionsToday : IncomesScreens(path = "income_screen")
        data object TransactionCreation : IncomesScreens(
            path = "transaction_creation_screen/income"
        )

        data object TransactionUpdate :
            IncomesScreens("transaction_update_screen/income/{id}") {
            fun transactionId(): String = "id"
            fun route(transactionId: Int) = "transaction_update_screen/income/$transactionId"
        }

        data object History : IncomesScreens(path = "history_screen/incomes")
    }

    sealed class ExpensesScreens(path: String) : Route(path) {
        data object TransactionsToday : ExpensesScreens(path = "expense_screen")
        data object TransactionCreation : ExpensesScreens(
            path = "transaction_creation_screen/expense"
        )

        data object TransactionUpdate :
            ExpensesScreens("transaction_update_screen/expense/{id}") {
            fun transactionId(): String = "id"
            fun route(transactionId: Int) = "transaction_update_screen/expense/$transactionId"
        }

        data object History : ExpensesScreens(path = "history_screen/expenses")
    }

    sealed class BalanceScreens(path: String) : Route(path) {
        data object Balance : BalanceScreens(path = "balance_screen")
        data object BalanceUpdate : BalanceScreens("balance_update/{balanceId}") {
            fun balanceId(): String = "balanceId"
            fun route(balanceId: Int) = "balance_update/$balanceId"
        }
    }
}