package com.example.shmr_finance_app_android.core.di

import com.example.shmr_finance_app_android.data.datasource.FinanceRemoteDataSource
import com.example.shmr_finance_app_android.data.repository.FinanceRepositoryImpl
import com.example.shmr_finance_app_android.domain.mapper.FinanceDomainMapper
import com.example.shmr_finance_app_android.domain.repository.FinanceRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideFinanceRepository(
        remoteDataSource: FinanceRemoteDataSource,
        domainMapper: FinanceDomainMapper
    ): FinanceRepository {
        return FinanceRepositoryImpl(remoteDataSource, domainMapper)
    }
}