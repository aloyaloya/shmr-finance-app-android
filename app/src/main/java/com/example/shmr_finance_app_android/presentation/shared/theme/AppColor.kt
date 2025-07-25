package com.example.shmr_finance_app_android.presentation.shared.theme

import androidx.compose.ui.graphics.Color

sealed class AppColor(
    val lightPrimary: Color,
    val lightSecondary: Color,
    val darkPrimary: Color,
    val darkSecondary: Color
) {
    data object Blue : AppColor(
        lightPrimary = Color(0xFFB1D9F0),
        lightSecondary = Color(0xFFD3F1FF),
        darkPrimary = Color(0xFF5891B1),
        darkSecondary = Color(0xFF3B7D9F)
    )

    data object Green : AppColor(
        lightPrimary = Color(0xFF83CF40),
        lightSecondary = Color(0xFFCCE5B9),
        darkPrimary = Color(0xFF5CB50E),
        darkSecondary = Color(0xFF48A200)
    )

    data object Red : AppColor(
        lightPrimary = Color(0xFFEA8476),
        lightSecondary = Color(0xFFFF9F96),
        darkPrimary = Color(0xFFBD5447),
        darkSecondary = Color(0xFFA73D33)
    )

    data object Purple : AppColor(
        lightPrimary = Color(0xFF8884FA),
        lightSecondary = Color(0xFFA8A1FF),
        darkPrimary = Color(0xFF595CCC),
        darkSecondary = Color(0xFF4049B6)
    )
}