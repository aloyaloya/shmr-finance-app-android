package com.example.shmr_finance_app_android.core.di

import com.example.shmr_finance_app_android.data.datasources.account.AccountLocalDataSource
import com.example.shmr_finance_app_android.data.datasources.account.AccountLocalDataSourceImpl
import com.example.shmr_finance_app_android.data.datasources.account.AccountRemoteDataSource
import com.example.shmr_finance_app_android.data.datasources.account.AccountRemoteDataSourceImpl
import com.example.shmr_finance_app_android.data.datasources.categories.CategoriesLocalDataSource
import com.example.shmr_finance_app_android.data.datasources.categories.CategoriesLocalDataSourceImpl
import com.example.shmr_finance_app_android.data.datasources.categories.CategoriesRemoteDataSource
import com.example.shmr_finance_app_android.data.datasources.categories.CategoriesRemoteDataSourceImpl
import com.example.shmr_finance_app_android.data.datasources.transactions.TransactionsLocalDataSource
import com.example.shmr_finance_app_android.data.datasources.transactions.TransactionsLocalDataSourceImpl
import com.example.shmr_finance_app_android.data.datasources.transactions.TransactionsRemoteDataSource
import com.example.shmr_finance_app_android.data.datasources.transactions.TransactionsRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Отвечает за предоставление реализаций удалённых источников данных (API)
 * в виде абстракций (интерфейсов) для всего приложения.
 */
@Module
internal interface DataSourceModule {

    /**
     * Предоставляет реализацию [AccountRemoteDataSource] для работы с API аккаунтов.
     * Заменяет абстракцию конкретным классом [AccountRemoteDataSourceImpl].
     */
    @Binds
    @Singleton
    fun bindAccountRemoteDataSource(
        accountRemoteDataSource: AccountRemoteDataSourceImpl
    ): AccountRemoteDataSource

    /**
     * Предоставляет реализацию [AccountLocalDataSource] для работы с DAO аккаунтов.
     * Заменяет абстракцию конкретным классом [AccountLocalDataSource].
     */
    @Binds
    @Singleton
    fun bindAccountLocalDataSource(
        accountLocalDataSource: AccountLocalDataSourceImpl
    ): AccountLocalDataSource

    /**
     * Предоставляет реализацию [CategoriesRemoteDataSource] для работы с API категорий.
     * Заменяет абстракцию конкретным классом [CategoriesRemoteDataSourceImpl].
     */
    @Binds
    @Singleton
    fun bindCategoriesRemoteDataSource(
        categoriesRemoteDataSource: CategoriesRemoteDataSourceImpl
    ): CategoriesRemoteDataSource

    /**
     * Предоставляет реализацию [CategoriesLocalDataSource] для работы с DAO категорий.
     * Заменяет абстракцию конкретным классом [CategoriesLocalDataSourceImpl].
     */
    @Binds
    @Singleton
    fun bindCategoriesLocalDataSource(
        categoriesLocalDataSource: CategoriesLocalDataSourceImpl
    ): CategoriesLocalDataSource

    /**
     * Предоставляет реализацию [TransactionsRemoteDataSource] для работы с API транзакций.
     * Заменяет абстракцию конкретным классом [TransactionsRemoteDataSourceImpl].
     */
    @Binds
    @Singleton
    fun bindTransactionsRemoteDataSource(
        transactionsRemoteDataSource: TransactionsRemoteDataSourceImpl
    ): TransactionsRemoteDataSource

    /**
     * Предоставляет реализацию [TransactionsLocalDataSource] для работы с DAO транзакций.
     * Заменяет абстракцию конкретным классом [TransactionsLocalDataSourceImpl].
     */
    @Binds
    @Singleton
    fun bindTransactionsLocalDataSource(
        transactionsLocalDataSource: TransactionsLocalDataSourceImpl
    ): TransactionsLocalDataSource
}