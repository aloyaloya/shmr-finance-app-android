package com.example.shmr_finance_app_android.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.work.Configuration
import androidx.work.WorkManager
import com.example.shmr_finance_app_android.data.sync.SyncPrefs
import com.example.shmr_finance_app_android.domain.repository.SettingsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Модуль, предоставляющий зависимости, связанные с приложением:
 * - Экземпляр [Application]
 * - Контекст приложения [Context]
 */
@Module
class AppModule(private val application: Application) {

    /** Предоставляет [Context] на уровне всего приложения */
    @Provides
    @Singleton
    fun provideContext(app: Application): Context = app.applicationContext

    @Provides
    @Singleton
    @Named("RegularPrefs")
    fun provideSharedPreferences(app: Application): SharedPreferences {
        return app.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    @Named("EncryptedPrefs")
    fun provideEncryptedSharedPreferences(context: Context): SharedPreferences {
        return EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    @Provides
    @Singleton
    fun provideHapticFeedbackManager(
        context: Context,
        settingsRepository: SettingsRepository
    ): HapticFeedbackManager {
        return HapticFeedbackManager(context, settingsRepository)
    }

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