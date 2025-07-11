package com.example.shmr_finance_app_android.core.di

import androidx.activity.ComponentActivity
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * CompositionLocal для доступа к текущей [ComponentActivity].
 * Используется в Compose-иерархии для внедрения активности.
 *
 * @throws IllegalStateException если [ComponentActivity] не предоставлена
 */
val LocalActivity = staticCompositionLocalOf<ComponentActivity> {
    error("No Activity provided!")
}

/**
 * CompositionLocal для доступа к текущему [ActivityComponent].
 * Используется в Compose-иерархии для внедрения зависимостей, привязанных к Activity.
 *
 * @throws IllegalStateException если [ActivityComponent] не предоставлен
 */
val LocalActivityComponent = staticCompositionLocalOf<ActivityComponent> {
    error("No ActivityComponent provided!")
}