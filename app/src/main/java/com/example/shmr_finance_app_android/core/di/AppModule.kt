package com.example.shmr_finance_app_android.core.di

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Модуль, предоставляющий зависимости, связанные с приложением:
 * - Экземпляр [Application]
 * - Контекст приложения [Context]
 */
@Module
class AppModule(private val application: Application) {

    /** Предоставляет экземпляр [Application] */
    @Provides
    @Singleton
    fun provideApplication(): Application = application

    /** Предоставляет [Context] на уровне всего приложения */
    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext
}