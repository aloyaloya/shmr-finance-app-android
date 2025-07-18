package com.example.shmr_finance_app_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shmr_finance_app_android.data.local.entities.AccountEntity

@Dao
interface AccountDao {

    @Query("SELECT * FROM accounts WHERE id = :accountId LIMIT 1")
    suspend fun getById(accountId: Int): AccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(account: AccountEntity)

    @Update
    suspend fun update(account: AccountEntity)

    @Query("UPDATE accounts SET syncStatus = 'SYNCED', updatedAt = :updatedAt WHERE id = :id")
    suspend fun markAsSynced(id: Int, updatedAt: String)

    @Query("SELECT * FROM accounts WHERE syncStatus = 'PENDING_UPDATE'")
    suspend fun getPendingUpdate(): AccountEntity?
}