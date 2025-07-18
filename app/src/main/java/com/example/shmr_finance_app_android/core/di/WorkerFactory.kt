package com.example.shmr_finance_app_android.core.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import javax.inject.Inject
import javax.inject.Provider

class AppWorkerFactory @Inject constructor(
    private val workers: Map<Class<out ListenableWorker>,
            @JvmSuppressWildcards Provider<ChildWorkerFactory>>
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {

        val foundEntry = workers.entries.find {
            Class.forName(workerClassName).isAssignableFrom(it.key)
        }

        return foundEntry?.value?.get()?.create(appContext, workerParameters)
    }
}

interface ChildWorkerFactory {
    fun create(appContext: Context, params: WorkerParameters): ListenableWorker
}