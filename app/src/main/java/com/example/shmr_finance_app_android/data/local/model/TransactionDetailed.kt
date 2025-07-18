package com.example.shmr_finance_app_android.data.local.model

import androidx.room.Embedded
import androidx.room.Relation
import com.example.shmr_finance_app_android.data.local.entities.AccountEntity
import com.example.shmr_finance_app_android.data.local.entities.CategoryEntity
import com.example.shmr_finance_app_android.data.local.entities.TransactionEntity

data class TransactionDetailed(
    @Embedded val transaction: TransactionEntity,

    @Relation(
        parentColumn = "accountId",
        entityColumn = "id"
    )
    val account: AccountEntity,

    @Relation(
        parentColumn = "categoryId",
        entityColumn = "id"
    )
    val category: CategoryEntity
)