package com.example.shmr_finance_app_android.data.local.mapper

import com.example.shmr_finance_app_android.data.local.entities.AccountEntity
import com.example.shmr_finance_app_android.data.model.AccountDTO
import com.example.shmr_finance_app_android.data.model.AccountResponseDTO
import dagger.Reusable
import javax.inject.Inject

/**
 * Маппер для:
 * Преобразования [AccountEntity] -> [AccountResponseDTO]
 * Преобразования [AccountEntity] -> [AccountDTO]
 * Преобразования [AccountResponseDTO] -> [AccountEntity]
 */
@Reusable
class AccountEntityMapper @Inject constructor() {
    fun mapAccountResponse(entity: AccountEntity): AccountResponseDTO {
        return AccountResponseDTO(
            id = entity.id,
            name = entity.name,
            balance = entity.balance,
            currency = entity.currency,
            incomeStats = emptyList(),
            expenseStats = emptyList(),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun mapAccount(entity: AccountEntity): AccountDTO {
        return AccountDTO(
            id = entity.id,
            userId = entity.id,
            name = entity.name,
            balance = entity.balance,
            currency = entity.currency,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    fun mapAccountResponseToEntity(account: AccountResponseDTO): AccountEntity {
        return AccountEntity(
            id = account.id,
            name = account.name,
            balance = account.balance,
            currency = account.currency,
            createdAt = account.createdAt,
            updatedAt = account.updatedAt
        )
    }
}