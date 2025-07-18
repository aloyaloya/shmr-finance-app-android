package com.example.shmr_finance_app_android.presentation.feature.main.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import com.example.shmr_finance_app_android.presentation.feature.main.viewmodel.MainScreenEvent
import com.example.shmr_finance_app_android.presentation.feature.main.viewmodel.MainScreenViewModel
import com.example.shmr_finance_app_android.presentation.shared.components.AnimatedSnackbar

@Composable
fun MainScreen() {
    val viewModel: MainScreenViewModel = daggerViewModel()
    val configState by viewModel.configState.collectAsState()

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route

    var snackbarMsg by remember { mutableIntStateOf(0) }
    var snackbarVisible by remember { mutableStateOf(false) }

    val errorColor = MaterialTheme.colorScheme.errorContainer
    val defaultColor = MaterialTheme.colorScheme.onTertiaryContainer

    val errorTextColor = MaterialTheme.colorScheme.onErrorContainer
    val defaultTextColor = MaterialTheme.colorScheme.onTertiary

    var snackbarBackgroundColor by remember { mutableStateOf(errorColor) }
    var snackbarTextColor by remember { mutableStateOf(errorTextColor) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is MainScreenEvent.ShowErrorSnackBar -> {
                    snackbarMsg = event.messageResId
                    snackbarVisible = true
                    snackbarBackgroundColor = errorColor
                    snackbarTextColor = errorTextColor
                }

                is MainScreenEvent.ShowDefaultSnackBar -> {
                    snackbarMsg = event.messageResId
                    snackbarVisible = true
                    snackbarBackgroundColor = defaultColor
                    snackbarTextColor = defaultTextColor
                }
            }
        }
    }

    Scaffold(
        topBar = {
            CustomTopBar(config = configState.topBarConfig)
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
            configState.floatingActionConfig?.let { action ->
                CustomFloatingActionButton(
                    description = action.descriptionResId,
                    onClick = { action.actionUnit.invoke() },
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AppNavHost(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                updateConfigState = { config -> viewModel.updateConfigForScreen(config) }
            )

            AnimatedSnackbar(
                modifier = Modifier.align(Alignment.TopCenter),
                isVisible = snackbarVisible,
                messageResId = snackbarMsg,
                onDismiss = { snackbarVisible = false },
                textColor = snackbarTextColor,
                backgroundColor = snackbarBackgroundColor
            )
        }
    }
}