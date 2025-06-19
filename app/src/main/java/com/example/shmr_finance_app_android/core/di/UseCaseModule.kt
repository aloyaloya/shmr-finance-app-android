package com.example.shmr_finance_app_android.core.di

import com.example.shmr_finance_app_android.domain.repository.FinanceRepository
import com.example.shmr_finance_app_android.domain.usecases.GetAccountUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetExpensesByPeriodUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesCategoriesUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetIncomesByPeriodUseCase
import com.example.shmr_finance_app_android.domain.usecases.GetTransactionsByPeriodUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetTransactionsByPeriodUseCase(
        repository: FinanceRepository
    ): GetTransactionsByPeriodUseCase {
        return GetTransactionsByPeriodUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetAccountUseCase(
        repository: FinanceRepository
    ): GetAccountUseCase {
        return GetAccountUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetIncomeCategoriesUseCase(
        repository: FinanceRepository
    ): GetIncomesCategoriesUseCase {
        return GetIncomesCategoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetIncomesTransactionsByPeriodUseCase(
        getTransactionsByPeriod: GetTransactionsByPeriodUseCase
    ): GetIncomesByPeriodUseCase {
        return GetIncomesByPeriodUseCase(getTransactionsByPeriod)
    }

    @Provides
    @Singleton
    fun provideGetExpensesTransactionsByPeriodUseCase(
        getTransactionsByPeriod: GetTransactionsByPeriodUseCase
    ): GetExpensesByPeriodUseCase {
        return GetExpensesByPeriodUseCase(getTransactionsByPeriod)
    }
}