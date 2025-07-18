package com.example.shmr_finance_app_android.data.local.mapper

import com.example.shmr_finance_app_android.data.local.entities.TransactionEntity
import com.example.shmr_finance_app_android.data.local.model.SyncStatus
import com.example.shmr_finance_app_android.data.local.model.TransactionDetailed
import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.CategoryDTO
import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.model.TransactionResponseDTO
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class TransactionEntityMapper @Inject constructor() {

    fun mapTransaction(entity: TransactionEntity): TransactionDTO {
        return TransactionDTO(
            id = entity.id,
            accountId = entity.accountId,
            categoryId = entity.categoryId,
            amount = entity.amount,
            transactionDate = entity.transactionDate,
            comment = entity.comment,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun mapTransactionResponse(entity: TransactionDetailed): TransactionResponseDTO {
        return TransactionResponseDTO(
            id = entity.transaction.id,
            account = AccountBriefDTO(
                id = entity.account.id,
                name = entity.account.name,
                balance = entity.account.balance,
                currency = entity.account.currency
            ),
            category = CategoryDTO(
                id = entity.category.id,
                name = entity.category.name,
                emoji = entity.category.emoji,
                isIncome = entity.category.isIncome
            ),
            amount = entity.transaction.amount,
            transactionDate = entity.transaction.transactionDate,
            comment = entity.transaction.comment,
            createdAt = entity.transaction.createdAt,
            updatedAt = entity.transaction.updatedAt
        )
    }

    fun mapTransactionResponseToEntity(dto: TransactionResponseDTO): TransactionEntity {
        return TransactionEntity(
            id = 0,
            serverId = dto.id,
            accountId = dto.account.id,
            categoryId = dto.category.id,
            amount = dto.amount,
            transactionDate = dto.transactionDate,
            comment = dto.comment,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt,
            syncStatus = SyncStatus.SYNCED
        )
    }

    fun mapTransactionDtoToResponse(dto: TransactionDTO): TransactionResponseDTO {
        return TransactionResponseDTO(
            id = dto.id,
            account = AccountBriefDTO(
                id = dto.accountId,
                name = "",
                balance = "",
                currency = ""
            ),
            category = CategoryDTO(
                id = dto.categoryId,
                name = "",
                emoji = "",
                isIncome = false
            ),
            amount = dto.amount,
            transactionDate = dto.transactionDate,
            comment = dto.comment,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }
}