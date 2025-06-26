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

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourceModule {

    @Binds
    @Singleton
    fun bindsAccountRemoteDataSource(
        accountRemoteDataSource: AccountRemoteDataSourceImpl
    ): AccountRemoteDataSource

    @Binds
    @Singleton
    fun bindsCategoriesRemoteDataSource(
        categoriesRemoteDataSource: CategoriesRemoteDataSourceImpl
    ): CategoriesRemoteDataSource

    @Binds
    @Singleton
    fun bindsTransactionsRemoteDataSource(
        transactionsRemoteDataSource: TransactionsRemoteDataSourceImpl
    ): TransactionsRemoteDataSource
}