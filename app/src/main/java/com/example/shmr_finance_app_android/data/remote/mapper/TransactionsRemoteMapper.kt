package com.example.shmr_finance_app_android.data.remote.mapper

import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.remote.model.AccountBriefResponse
import com.example.shmr_finance_app_android.data.remote.model.TransactionResponse
import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class TransactionsRemoteMapper @Inject constructor(
    private val categoryMapper: CategoryRemoteMapper
) {
    fun mapTransaction(response: TransactionResponse): TransactionDTO {
        return TransactionDTO(
            id = response.id,
            account = mapAccountBrief(response.account),
            category = categoryMapper.mapCategory(response.category),
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