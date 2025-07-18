package com.example.shmr_finance_app_android.core.di

import android.content.Context
import androidx.room.Room
import com.example.shmr_finance_app_android.data.local.FinanceDatabase
import com.example.shmr_finance_app_android.data.local.dao.AccountDao
import com.example.shmr_finance_app_android.data.local.dao.CategoryDao
import com.example.shmr_finance_app_android.data.local.dao.TransactionDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideFinanceDatabase(context: Context): FinanceDatabase {
        return Room.databaseBuilder(
            context,
            FinanceDatabase::class.java,
            "finance.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideAccountDao(database: FinanceDatabase): AccountDao {
        return database.accountDao()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(database: FinanceDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Singleton
    @Provides
    fun provideTransactionDao(database: FinanceDatabase): TransactionDao {
        return database.transactionDao()
    }
}