package com.example.shmr_finance_app_android.presentation.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    tertiary = BlackGreen,
    onTertiary = White,
)

private val LightColorScheme = lightColorScheme(
    tertiary = Green,
    onTertiary = Black,
    onTertiaryContainer = LightGreen,
    tertiaryContainer = DarkGray,
    errorContainer = LightRed,
    onErrorContainer = White

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun ShmrfinanceappandroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    appColor: AppColor,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            tertiary = appColor.darkPrimary,
            onTertiaryContainer = appColor.darkSecondary
        )
    } else {
        lightColorScheme(
            tertiary = appColor.lightPrimary,
            onTertiaryContainer = appColor.lightSecondary
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}