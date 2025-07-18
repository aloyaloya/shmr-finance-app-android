package com.example.shmr_finance_app_android.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shmr_finance_app_android.core.di.ChildWorkerFactory
import com.example.shmr_finance_app_android.core.network.NetworkChecker
import com.example.shmr_finance_app_android.domain.usecases.SyncUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SyncAllWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val syncUseCase: SyncUseCase,
    private val networkChecker: NetworkChecker,
    private val syncPrefs: SyncPrefs
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            if (!networkChecker.isNetworkAvailable()) {
                return if (runAttemptCount < 3) Result.retry() else Result.failure()
            }

            syncUseCase.syncAllData()

            syncPrefs.saveLastSyncTime(System.currentTimeMillis())
            Result.success()

        } catch (_: Exception) {
            Result.failure()
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParams: WorkerParameters
        ): SyncAllWorker
    }
}