package com.example.shmr_finance_app_android.di

import com.example.shmr_finance_app_android.data.mapper.FinanceRemoteMapper
import com.example.shmr_finance_app_android.domain.mapper.FinanceDomainMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {

    @Provides
    @Singleton
    fun provideFinanceApiMapper(): FinanceRemoteMapper {
        return FinanceRemoteMapper()
    }

    @Provides
    @Singleton
    fun provideFinanceDomainMapper(): FinanceDomainMapper {
        return FinanceDomainMapper()
    }
}