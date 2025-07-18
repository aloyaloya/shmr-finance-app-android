package com.example.shmr_finance_app_android.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.shmr_finance_app_android.data.local.model.SyncStatus

@Entity(
    tableName = "transactions",
    indices = [
        Index("serverId", unique = true),
        Index("accountId"),
        Index("categoryId")
    ],
    foreignKeys = [
        ForeignKey(
            entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val serverId: Int? = null,

    val accountId: Int,
    val categoryId: Int,

    val amount: String,
    val transactionDate: String,

    val comment: String? = null,

    val createdAt: String,
    val updatedAt: String,

    @ColumnInfo(defaultValue = "'PENDING_CREATE'")
    val syncStatus: SyncStatus = SyncStatus.PENDING_CREATE
)