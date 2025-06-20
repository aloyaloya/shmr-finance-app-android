package com.example.shmr_finance_app_android.core.di

import com.example.shmr_finance_app_android.data.repository.FinanceRepositoryImpl
import com.example.shmr_finance_app_android.domain.repository.FinanceRepository
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
    fun provideFinanceRepository(
        financeRepository: FinanceRepositoryImpl,
    ): FinanceRepository
}