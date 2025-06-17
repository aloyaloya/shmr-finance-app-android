package com.example.shmr_finance_app_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shmr_finance_app_android.ui.screens.main_screen.MainScreen
import com.example.shmr_finance_app_android.ui.theme.ShmrfinanceappandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            ShmrfinanceappandroidTheme(darkTheme = false, dynamicColor = false) {
                MainScreen()
            }
        }
    }
}