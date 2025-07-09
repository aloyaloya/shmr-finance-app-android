package com.example.shmr_finance_app_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shmr_finance_app_android.core.di.LocalActivityComponent
import com.example.shmr_finance_app_android.presentation.feature.main.screen.MainScreen
import com.example.shmr_finance_app_android.presentation.shared.theme.ShmrfinanceappandroidTheme

class MainActivity : ComponentActivity() {
    private val activityComponent by lazy {
        (application as App).appComponent
            .activityComponent()
            .create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(
                LocalActivity provides this,
                LocalActivityComponent provides activityComponent
            ) {
                ShmrfinanceappandroidTheme(darkTheme = false, dynamicColor = false) {
                    MainScreen()
                }
            }
        }
    }
}