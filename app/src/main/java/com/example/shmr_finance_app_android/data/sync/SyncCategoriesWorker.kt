package com.example.shmr_finance_app_android.data.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shmr_finance_app_android.core.di.ChildWorkerFactory
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SyncCategoriesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val categoriesRepository: CategoriesRepository
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            categoriesRepository.syncCategories().getOrThrow()
            Result.success()
        } catch (e: Exception) {
            Log.e("CategoriesWorker", e.toString())
            if (runAttemptCount < 3) Result.retry() else Result.failure()
        }
    }

    @AssistedFactory
    interface Factory : ChildWorkerFactory {
        override fun create(
            appContext: Context,
            params: WorkerParameters
        ): SyncCategoriesWorker
    }
}