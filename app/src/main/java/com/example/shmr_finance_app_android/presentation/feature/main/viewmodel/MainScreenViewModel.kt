package com.example.shmr_finance_app_android.presentation.feature.main.viewmodel

import androidx.lifecycle.ViewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.main.model.FloatingActionConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * ViewModel главного экрана, отвечающая за:
 * - Управление конфигурацией UI (TopAppBar, FAB) для текущего экрана
 * - Централизованное обновление состояния навигации
 */
class MainScreenViewModel @Inject constructor() : ViewModel() {

    private val _configState = MutableStateFlow(
        ScreenConfig(
            route = Route.Root.Expenses.path,
            topBarConfig = TopBarConfig(
                titleResId = R.string.expense_screen_title,
                action = TopBarAction(
                    iconResId = R.drawable.ic_history,
                    descriptionResId = R.string.expenses_history_description,
                    actionRoute = Route.SubScreens.History.route(income = false)
                )
            ),
            floatingActionConfig = FloatingActionConfig(
                descriptionResId = R.string.add_expense_description,
                actionRoute = Route.Root.Expenses.path
            )
        )
    )

    val configState: StateFlow<ScreenConfig> = _configState

    /**
     * Обновляет конфигурацию для указанного экрана.
     * Используется для синхронизации:
     * - Заголовка TopAppBar
     * - Иконки действия
     * - FAB
     */
    fun updateConfigForScreen(config: ScreenConfig) {
        _configState.value = config
    }
}