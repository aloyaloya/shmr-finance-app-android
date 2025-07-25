package com.example.shmr_finance_app_android

import android.app.Application
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.shmr_finance_app_android.core.di.AppComponent
import com.example.shmr_finance_app_android.core.di.AppModule
import com.example.shmr_finance_app_android.core.di.AppWorkerFactory
import com.example.shmr_finance_app_android.core.di.DaggerAppComponent
import com.example.shmr_finance_app_android.core.network.NetworkMonitor
import com.example.shmr_finance_app_android.data.sync.SyncAllWorker
import com.example.shmr_finance_app_android.domain.repository.SettingsRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: AppWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private lateinit var networkMonitor: NetworkMonitor

    @Inject
    lateinit var settingsRepository: SettingsRepository

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .application(this)
            .appModule(AppModule(this))
            .build()

        appComponent.inject(this)

        networkMonitor = NetworkMonitor(this)
        networkMonitor.registerNetworkCallback()

        startImmediateSync()
        setupPeriodicSync()
    }

    private fun startImmediateSync() {
        val constraints = Constraints.Builder().build()

        val immediateSyncRequest = OneTimeWorkRequestBuilder<SyncAllWorker>()
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "sync_work",
            ExistingWorkPolicy.KEEP,
            immediateSyncRequest
        )
    }

    private fun setupPeriodicSync() {
        val minutes = settingsRepository.getSyncFrequency()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncAllWorker>(
            minutes.toLong(),
            TimeUnit.MINUTES
        ).setConstraints(constraints)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "sync_work",
                ExistingPeriodicWorkPolicy.KEEP,
                syncRequest
            )
    }
}