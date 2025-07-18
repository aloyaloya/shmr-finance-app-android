package com.example.shmr_finance_app_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.shmr_finance_app_android.data.local.entities.TransactionEntity
import com.example.shmr_finance_app_android.data.local.model.TransactionDetailed

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactions: List<TransactionEntity>)

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Transaction
    @Query(
        """
    SELECT * FROM transactions 
    WHERE id = :id
    LIMIT 1
"""
    )
    suspend fun getById(id: Int): TransactionDetailed?

    @Query("SELECT * FROM transactions WHERE serverId = :serverId LIMIT 1")
    suspend fun getByServerId(serverId: Int): TransactionEntity?

    @Transaction
    @Query(
        """
    SELECT * FROM transactions 
    WHERE accountId = :accountId 
    AND transactionDate BETWEEN :start AND :end
    AND syncStatus != 'PENDING_DELETE'
    ORDER BY transactionDate DESC
"""
    )
    suspend fun getByPeriod(
        accountId: Int,
        start: String,
        end: String
    ): List<TransactionDetailed>

    @Query("SELECT * FROM transactions WHERE syncStatus IN ('PENDING_CREATE', 'PENDING_UPDATE')")
    suspend fun getPendingSync(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE syncStatus = 'PENDING_DELETE'")
    suspend fun getPendingDeletions(): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE syncStatus IN ('SYNCED', 'PENDING_UPDATE')")
    suspend fun getSyncedOrPendingUpdateTransactions(): List<TransactionEntity>

    @Query("UPDATE transactions SET syncStatus = 'SYNCED', serverId = :serverId, updatedAt = :updatedAt WHERE id = :localId")
    suspend fun markAsSynced(localId: Int, serverId: Int, updatedAt: String)

    @Query("UPDATE transactions SET syncStatus = 'SYNCED' WHERE id = :id")
    suspend fun markSyncedById(id: Int)

    @Query("DELETE FROM transactions WHERE syncStatus = 'PENDING_DELETE' AND serverId IS NOT NULL")
    suspend fun purgeDeletedSynced()

    @Query("SELECT * FROM transactions WHERE syncStatus = :status")
    suspend fun getAllWithStatus(status: String): List<TransactionEntity>

    @Query("UPDATE transactions SET syncStatus = :status WHERE id = :localId")
    suspend fun updateSyncStatus(localId: Int, status: String)
}