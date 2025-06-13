package com.example.shmr_finance_app_android.ui.screens.main_screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shmr_finance_app_android.navigation.AppNavHost
import com.example.shmr_finance_app_android.navigation.RootScreen
import com.example.shmr_finance_app_android.ui.navigation.BottomNavigationBar
import com.example.shmr_finance_app_android.ui.navigation.CustomFloatingActionButton
import com.example.shmr_finance_app_android.ui.navigation.CustomTopBar
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarViewModel

@Composable
fun MainScreen() {
    val topBarViewModel: TopBarViewModel = viewModel()
    val topBarState by topBarViewModel.state.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    val currentScreen = RootScreen.fromRoute(currentDestination)

    LaunchedEffect(currentDestination) {
        currentScreen?.let { screen ->
            topBarViewModel.updateStateForScreen(screen)
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(
                state = topBarState,
                onBack = {
                    navController.popBackStack()
                },
                onActionRoute = {
//                    navController.navigate(it) // Дальнейшие экраны еще не делали
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentDestination = currentDestination,
                onNavigate = {
                    navController.navigate(it) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                screens = RootScreen.screens
            )
        },
        floatingActionButton = {
            currentScreen?.onFloatingActionRoute?.let { route ->
                CustomFloatingActionButton(
                    onClick = {
//                        navController.navigate(route) // Дальнейшие экраны еще не делали
                    },
                    description = currentScreen.floatingActionDescription
                )
            }
        }
    ) { innerPadding ->
        AppNavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController
        )
    }
}