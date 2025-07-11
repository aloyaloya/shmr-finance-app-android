package com.example.shmr_finance_app_android.data.remote.mapper

import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.AccountDTO
import com.example.shmr_finance_app_android.data.model.AccountResponseDTO
import com.example.shmr_finance_app_android.data.model.StatItemDTO
import com.example.shmr_finance_app_android.data.remote.model.Account
import com.example.shmr_finance_app_android.data.remote.model.AccountResponse
import com.example.shmr_finance_app_android.data.remote.model.AccountUpdateRequest
import com.example.shmr_finance_app_android.data.remote.model.StatItem
import dagger.Reusable
import javax.inject.Inject

/**
 * Маппер для:
 * Преобразования [AccountResponse] -> [AccountResponseDTO] при получении данных
 * Преобразования [AccountBriefDTO] -> [AccountUpdateRequest] при изменении данных
 * Создает модель данных статистики аккаунта [StatItemDTO]
 */
@Reusable
internal class AccountRemoteMapper @Inject constructor() {
    fun mapAccountResponse(response: AccountResponse): AccountResponseDTO {
        return AccountResponseDTO(
            id = response.id,
            name = response.name,
            balance = response.balance,
            currency = response.currency,
            incomeStats = response.incomeStats.map { mapStatItem(it) },
            expenseStats = response.expenseStats.map { mapStatItem(it) },
            createdAt = response.createdAt,
            updatedAt = response.updatedAt
        )
    }

    private fun mapStatItem(item: StatItem): StatItemDTO {
        return StatItemDTO(
            categoryId = item.categoryId,
            categoryName = item.categoryName,
            emoji = item.emoji,
            amount = item.amount
        )
    }

    fun mapAccount(account: Account): AccountDTO {
        return AccountDTO(
            id = account.id,
            userId = account.userId,
            name = account.name,
            balance = account.balance,
            currency = account.currency,
            createdAt = account.createdAt,
            updatedAt = account.updatedAt
        )
    }

    fun mapAccountBrief(brief: AccountBriefDTO): AccountUpdateRequest {
        return AccountUpdateRequest(
            name = brief.name,
            balance = brief.balance,
            currency = brief.currency
        )
    }
}