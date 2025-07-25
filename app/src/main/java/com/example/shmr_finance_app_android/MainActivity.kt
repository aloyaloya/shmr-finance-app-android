package com.example.shmr_finance_app_android

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.shmr_finance_app_android.core.di.LocalActivityComponent
import com.example.shmr_finance_app_android.core.utils.LocaleHelper
import com.example.shmr_finance_app_android.presentation.feature.auth.AuthScreen
import com.example.shmr_finance_app_android.presentation.feature.main.screen.MainScreen
import com.example.shmr_finance_app_android.presentation.shared.theme.ShmrfinanceappandroidTheme
import com.example.shmr_finance_app_android.presentation.shared.theme.ThemeViewModel

class MainActivity : ComponentActivity() {
    private val activityComponent by lazy {
        (application as App).appComponent
            .activityComponent()
            .create()
    }

    private val themeViewModel: ThemeViewModel by viewModels {
        activityComponent.viewModelProvider()
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "ru") ?: "ru"
        val localizedContext = LocaleHelper.setLocale(newBase, lang)
        super.attachBaseContext(localizedContext)
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
                val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
                val appColor by themeViewModel.appColor.collectAsState()

                var isAuthenticated by remember { mutableStateOf(false) }

                ShmrfinanceappandroidTheme(
                    darkTheme = isDarkTheme,
                    appColor = appColor
                ) {
                    if (isAuthenticated) {
                        MainScreen()
                    } else {
                        AuthScreen(onAuthenticated = { isAuthenticated = true })
                    }
                }
            }
        }
    }
}