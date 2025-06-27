package com.example.shmr_finance_app_android.core.di

import com.example.shmr_finance_app_android.data.datasource.AccountRemoteDataSource
import com.example.shmr_finance_app_android.data.datasource.AccountRemoteDataSourceImpl
import com.example.shmr_finance_app_android.data.datasource.CategoriesRemoteDataSource
import com.example.shmr_finance_app_android.data.datasource.CategoriesRemoteDataSourceImpl
import com.example.shmr_finance_app_android.data.datasource.TransactionsRemoteDataSource
import com.example.shmr_finance_app_android.data.datasource.TransactionsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Отвечает за предоставление реализаций удалённых источников данных (API)
 * в виде абстракций (интерфейсов) для всего приложения.
 */
@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {

    /**
     * Предоставляет реализацию [AccountRemoteDataSource] для работы с API аккаунтов.
     * Заменяет абстракцию конкретным классом [AccountRemoteDataSourceImpl].
     */
    @Binds
    @Singleton
    fun bindsAccountRemoteDataSource(
        accountRemoteDataSource: AccountRemoteDataSourceImpl
    ): AccountRemoteDataSource

    /**
     * Предоставляет реализацию [CategoriesRemoteDataSource] для работы с API категорий.
     * Заменяет абстракцию конкретным классом [CategoriesRemoteDataSourceImpl].
     */
    @Binds
    @Singleton
    fun bindsCategoriesRemoteDataSource(
        categoriesRemoteDataSource: CategoriesRemoteDataSourceImpl
    ): CategoriesRemoteDataSource

    /**
     * Предоставляет реализацию [TransactionsRemoteDataSource] для работы с API транзакций.
     * Заменяет абстракцию конкретным классом [TransactionsRemoteDataSourceImpl].
     */
    @Binds
    @Singleton
    fun bindsTransactionsRemoteDataSource(
        transactionsRemoteDataSource: TransactionsRemoteDataSourceImpl
    ): TransactionsRemoteDataSource
}