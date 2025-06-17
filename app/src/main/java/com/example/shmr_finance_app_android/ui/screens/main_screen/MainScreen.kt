package com.example.shmr_finance_app_android.ui.screens.main_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shmr_finance_app_android.navigation.components.AppNavHost
import com.example.shmr_finance_app_android.navigation.components.BottomNavigationBar
import com.example.shmr_finance_app_android.navigation.components.CustomFloatingActionButton
import com.example.shmr_finance_app_android.navigation.components.CustomTopBar
import com.example.shmr_finance_app_android.navigation.routes.BottomBarItem

@Composable
fun MainScreen() {
    val viewModel: MainScreenViewModel = viewModel()
    val configState by viewModel.configState.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            CustomTopBar(
                config = configState.topBarConfig,
                onBack = { navController.popBackStack() },
                onActionRoute = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                items = BottomBarItem.items,
                onNavigate = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        },
        floatingActionButton = {
            configState.floatingActionConfig?.let {
                CustomFloatingActionButton(
                    description = it.descriptionResId,
                    onClick = {}, // Дальнейшие экраны еще не делали
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            updateConfigState = { config -> viewModel.updateConfigForScreen(config) }
        )
    }
}