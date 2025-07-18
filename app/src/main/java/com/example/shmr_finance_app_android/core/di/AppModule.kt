package com.example.shmr_finance_app_android.core.di

import android.app.Application
import android.content.Context
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.shmr_finance_app_android.data.sync.SyncPrefs
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


    @Provides
    @Singleton
    fun provideSyncPrefs(context: Context): SyncPrefs = SyncPrefs(context)

    @Provides
    @Singleton
    fun provideWorkManager(context: Context): WorkManager {
        return WorkManager.getInstance(context)
    }

    /** Предоставляет конфигурацию WorkManager */
    @Provides
    @Singleton
    fun provideWorkManagerConfiguration(workerFactory: AppWorkerFactory): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}