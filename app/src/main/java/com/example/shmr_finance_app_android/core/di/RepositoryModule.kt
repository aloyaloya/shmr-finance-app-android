package com.example.shmr_finance_app_android.core.di

import com.example.shmr_finance_app_android.data.repository.AccountRepositoryImpl
import com.example.shmr_finance_app_android.data.repository.CategoriesRepositoryImpl
import com.example.shmr_finance_app_android.data.repository.TransactionsRepositoryImpl
import com.example.shmr_finance_app_android.domain.repository.AccountRepository
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    @Singleton
    fun bindsAccountRepository(
        accountRepository: AccountRepositoryImpl,
    ): AccountRepository

    @Binds
    @Singleton
    fun bindsCategoriesRepository(
        categoriesRepository: CategoriesRepositoryImpl,
    ): CategoriesRepository

    @Binds
    @Singleton
    fun bindsTransactionsRepository(
        transactionsRepository: TransactionsRepositoryImpl,
    ): TransactionsRepository
}