package com.example.shmr_finance_app_android.core.utils

import android.content.Context
import java.util.Locale

object LocaleHelper {

    fun setLocale(context: Context, language: String): Context {
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    fun applyLocaleToApplication(context: Context, language: String) {
        val appContext = context.applicationContext
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = appContext.resources.configuration
        config.setLocale(locale)
        appContext.resources.updateConfiguration(config, appContext.resources.displayMetrics)
    }
}