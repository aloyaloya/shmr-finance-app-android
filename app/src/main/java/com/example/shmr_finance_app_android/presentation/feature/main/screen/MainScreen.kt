package com.example.shmr_finance_app_android.presentation.feature.main.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shmr_finance_app_android.core.di.daggerViewModel
import com.example.shmr_finance_app_android.core.navigation.AppNavHost
import com.example.shmr_finance_app_android.core.navigation.BottomBarItem
import com.example.shmr_finance_app_android.presentation.feature.main.component.BottomNavigationBar
import com.example.shmr_finance_app_android.presentation.feature.main.component.CustomFloatingActionButton
import com.example.shmr_finance_app_android.presentation.feature.main.component.CustomTopBar
import com.example.shmr_finance_app_android.presentation.feature.main.viewmodel.MainScreenViewModel

@Composable
fun MainScreen() {
    val viewModel: MainScreenViewModel = daggerViewModel()
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
                    if (currentDestination != it) {
                        navController.navigate(it) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
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