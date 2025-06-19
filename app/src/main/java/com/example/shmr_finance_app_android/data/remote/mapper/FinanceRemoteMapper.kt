package com.example.shmr_finance_app_android.data.remote.mapper

import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.AccountDTO
import com.example.shmr_finance_app_android.data.model.CategoryDTO
import com.example.shmr_finance_app_android.data.model.StatItemDTO
import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.remote.model.AccountBriefResponse
import com.example.shmr_finance_app_android.data.remote.model.AccountResponse
import com.example.shmr_finance_app_android.data.remote.model.CategoryResponse
import com.example.shmr_finance_app_android.data.remote.model.StatItemResponse
import com.example.shmr_finance_app_android.data.remote.model.TransactionResponse
import javax.inject.Inject

class FinanceRemoteMapper @Inject constructor() {
    fun mapAccount(response: AccountResponse): AccountDTO {
        return AccountDTO(
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

    private fun mapStatItem(item: StatItemResponse): StatItemDTO {
        return StatItemDTO(
            categoryId = item.categoryId,
            categoryName = item.categoryName,
            emoji = item.emoji,
            amount = item.amount
        )
    }

    fun mapCategory(response: CategoryResponse): CategoryDTO {
        return CategoryDTO(
            id = response.id,
            name = response.name,
            emoji = response.emoji,
            isIncome = response.isIncome,
        )
    }

    fun mapTransaction(response: TransactionResponse): TransactionDTO {
        return TransactionDTO(
            id = response.id,
            account = mapAccountBrief(response.account),
            category = mapCategory(response.category),
            amount = response.amount,
            transactionDate = response.transactionDate,
            comment = response.comment,
            createdAt = response.createdAt,
            updatedAt = response.updatedAt
        )
    }

    private fun mapAccountBrief(brief: AccountBriefResponse): AccountBriefDTO {
        return AccountBriefDTO(
            id = brief.id,
            name = brief.name,
            balance = brief.balance,
            currency = brief.currency
        )
    }
}