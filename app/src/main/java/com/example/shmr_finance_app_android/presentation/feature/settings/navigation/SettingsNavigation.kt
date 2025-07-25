package com.example.shmr_finance_app_android.presentation.feature.settings.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shmr_finance_app_android.core.navigation.Route
import com.example.shmr_finance_app_android.presentation.feature.auth.AuthScreen
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.settings.screen.SettingsScreen

@Composable
fun SettingsNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    updateConfigState: (ScreenConfig) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.SettingsScreens.Settings.path
    ) {

        /**
         * Отвечает за:
         * 1. Отображение экрана Cчет
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на экран Редактирования счета
         */
        /**
         * Отвечает за:
         * 1. Отображение экрана Cчет
         * 2. Передачу конфигурации топ-бара
         * 3. Обработку навигации на экран Редактирования счета
         */
        composable(route = Route.SettingsScreens.Settings.path) {
            SettingsScreen(
                updateConfigState = updateConfigState,
                onPinUpdateNavigate = {
                    navController.navigate(Route.SettingsScreens.PinSettings.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable(route = Route.SettingsScreens.PinSettings.path) {
            AuthScreen(
                onAuthenticated = {
                    navController.navigate(Route.SettingsScreens.Settings.path) {
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                isChangePinMode = true,
                updateConfigState = updateConfigState
            )
        }
    }
}