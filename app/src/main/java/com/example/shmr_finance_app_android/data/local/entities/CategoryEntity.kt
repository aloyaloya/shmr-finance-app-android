package com.example.shmr_finance_app_android.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,

    val name: String,
    val emoji: String,
    val isIncome: Boolean
)