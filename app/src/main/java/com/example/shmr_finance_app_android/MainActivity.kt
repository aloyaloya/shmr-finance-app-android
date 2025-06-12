package com.example.shmr_finance_app_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.shmr_finance_app_android.navigation.AppNavHost
import com.example.shmr_finance_app_android.navigation.RootScreen
import com.example.shmr_finance_app_android.ui.navigation.BottomNavigationBar
import com.example.shmr_finance_app_android.ui.navigation.CustomTopBar
import com.example.shmr_finance_app_android.ui.theme.ShmrfinanceappandroidTheme
import com.example.shmr_finance_app_android.ui.viewmodels.TopBarViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShmrfinanceappandroidTheme(darkTheme = false, dynamicColor = false) {
                val topBarViewModel: TopBarViewModel = viewModel()
                val navController = rememberNavController()
                val topBarState by topBarViewModel.state.collectAsState()

                Scaffold(
                    topBar = {
                        CustomTopBar(
                            state = topBarState,
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(
                            navController = navController,
                            screens = RootScreen.screens
                        )
                    }
                ) { innerPadding ->
                    AppNavHost(
                        modifier = Modifier.padding(innerPadding),
                        topBarViewModel = topBarViewModel,
                        navController = navController
                    )
                }
            }
        }
    }
}