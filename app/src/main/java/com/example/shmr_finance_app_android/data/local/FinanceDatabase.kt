package com.example.shmr_finance_app_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.shmr_finance_app_android.data.local.dao.AccountDao
import com.example.shmr_finance_app_android.data.local.dao.CategoryDao
import com.example.shmr_finance_app_android.data.local.dao.TransactionDao
import com.example.shmr_finance_app_android.data.local.entities.AccountEntity
import com.example.shmr_finance_app_android.data.local.entities.CategoryEntity
import com.example.shmr_finance_app_android.data.local.entities.TransactionEntity

@Database(
    entities = [
        AccountEntity::class,
        CategoryEntity::class,
        TransactionEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun accountDao(): AccountDao
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}