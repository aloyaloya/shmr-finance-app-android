package com.example.shmr_finance_app_android.presentation.feature.settings.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.shared.theme.AppColor

data class PropertyModel(
    val id: String,
    @StringRes val titleResId: Int,
    @DrawableRes val iconResId: Int? = null,
    val displayColor: Color? = null
)
//    fun toListItem(): ListItem = ListItem(
//        lead = displayColor?.let { LeadContent.Text(text = "", color = displayColor) },
//        content = MainContent(title = title)
//    )

object PropertyModels {
    fun fromAppColor(appColor: AppColor, isDarkTheme: Boolean): PropertyModel = PropertyModel(
        id = when (appColor) {
            is AppColor.Blue -> "Blue"
            is AppColor.Green -> "Green"
            is AppColor.Red -> "Red"
            is AppColor.Purple -> "Purple"
        },
        titleResId = when (appColor) {
            is AppColor.Blue -> R.string.blue_color
            is AppColor.Green -> R.string.green_color
            is AppColor.Red -> R.string.red_color
            is AppColor.Purple -> R.string.purple_color
        },
        displayColor = if (isDarkTheme) appColor.darkPrimary else appColor.lightPrimary
    )

    fun getColors(isDarkTheme: Boolean): List<PropertyModel> = listOf(
        fromAppColor(AppColor.Blue, isDarkTheme),
        fromAppColor(AppColor.Green, isDarkTheme),
        fromAppColor(AppColor.Red, isDarkTheme),
        fromAppColor(AppColor.Purple, isDarkTheme)
    )

    fun getLanguages(): List<PropertyModel> = listOf(
        PropertyModel(id = "ru", titleResId = R.string.ru_language),
        PropertyModel(id = "en", titleResId = R.string.en_language)
    )

    fun getHaptics(): List<PropertyModel> = listOf(
        PropertyModel(
            id = HapticStrength.STRONG.name.lowercase(),
            titleResId = R.string.strong
        ),
        PropertyModel(
            id = HapticStrength.MEDIUM.name.lowercase(),
            titleResId = R.string.medium
        ),
        PropertyModel(
            id = HapticStrength.WEAK.name.lowercase(),
            titleResId = R.string.weak
        ),
        PropertyModel(
            id = HapticStrength.OFF.name.lowercase(),
            titleResId = R.string.off
        )
    )
}

enum class HapticStrength {
    STRONG, MEDIUM, WEAK, OFF
}
