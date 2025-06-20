package com.example.shmr_finance_app_android.core.di

import com.example.shmr_finance_app_android.data.remote.mapper.FinanceRemoteMapper
import com.example.shmr_finance_app_android.data.remote.api.FinanceApiService
import com.example.shmr_finance_app_android.data.datasource.FinanceRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideFinanceRemoteDataSource(
        apiService: FinanceApiService,
        mapper: FinanceRemoteMapper
    ): FinanceRemoteDataSource {
        return FinanceRemoteDataSource(apiService, mapper)
    }
}