package com.example.shmr_finance_app_android.core.di

import androidx.work.ListenableWorker
import com.example.shmr_finance_app_android.data.sync.SyncAccountWorker
import com.example.shmr_finance_app_android.data.sync.SyncAllWorker
import com.example.shmr_finance_app_android.data.sync.SyncCategoriesWorker
import com.example.shmr_finance_app_android.data.sync.SyncTransactionsWorker
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import kotlin.reflect.KClass

@Module
abstract class WorkersModule {

    @Binds
    @IntoMap
    @WorkerKey(SyncCategoriesWorker::class)
    abstract fun bindSyncCategoriesWorker(
        factory: SyncCategoriesWorker.Factory
    ): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SyncAccountWorker::class)
    abstract fun bindSyncAccountsWorker(
        factory: SyncAccountWorker.Factory
    ): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SyncTransactionsWorker::class)
    abstract fun bindSyncTransactionsWorker(
        factory: SyncTransactionsWorker.Factory
    ): ChildWorkerFactory

    @Binds
    @IntoMap
    @WorkerKey(SyncAllWorker::class)
    abstract fun bindSyncWorker(
        factory: SyncAllWorker.Factory
    ): ChildWorkerFactory
}

@MapKey
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class WorkerKey(val value: KClass<out ListenableWorker>)