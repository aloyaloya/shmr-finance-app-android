package com.example.shmr_finance_app_android.core.di

import androidx.lifecycle.ViewModelProvider
import dagger.Subcomponent
import javax.inject.Scope

/**
 * Область действия зависимостей, привязанная к жизненному циклу Activity.
 * Используется для создания и управления зависимостями внутри одной Activity.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

/**
 * Dagger Subcomponent, предоставляющий зависимости, живущие в рамках одной Activity:
 * - Использует модуль [ViewModelModule] для предоставления ViewModel-ей
 * - Предоставляет [ViewModelProvider.Factory] для создания ViewModel-ей
 */
@ActivityScope
@Subcomponent(modules = [ViewModelModule::class])
interface ActivityComponent {

    /** Фабрика для создания экземпляра [ActivityComponent] */
    @Subcomponent.Factory
    interface Factory {
        fun create(): ActivityComponent
    }

    /**
     * Предоставляет [ViewModelProvider.Factory] для создания ViewModel'ей,
     * привязанных к данной Activity
     */
    fun viewModelProvider(): ViewModelProvider.Factory
}