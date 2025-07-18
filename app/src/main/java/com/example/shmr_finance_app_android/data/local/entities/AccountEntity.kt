package com.example.shmr_finance_app_android.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shmr_finance_app_android.data.local.model.SyncStatus

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val serverId: Int? = null,

    val name: String,
    val balance: String,
    val currency: String,

    val createdAt: String,
    val updatedAt: String,

    @ColumnInfo(defaultValue = "'SYNCED'")
    val syncStatus: SyncStatus = SyncStatus.SYNCED
)