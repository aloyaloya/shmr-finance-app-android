package com.example.shmr_finance_app_android.domain.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shmr_finance_app_android.data.dto.AccountBriefDTO
import com.example.shmr_finance_app_android.data.dto.AccountDTO
import com.example.shmr_finance_app_android.data.dto.CategoryDTO
import com.example.shmr_finance_app_android.data.dto.StatItemDTO
import com.example.shmr_finance_app_android.data.dto.TransactionDTO
import com.example.shmr_finance_app_android.domain.model.AccountBriefDomain
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.model.CategoryDomain
import com.example.shmr_finance_app_android.domain.model.StatItemDomain
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class FinanceDomainMapper @Inject constructor() {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapAccount(dto: AccountDTO): AccountDomain {
        val createdAt = Instant.parse(dto.createdAt).atZone(ZoneId.systemDefault())
        val updatedAt = Instant.parse(dto.updatedAt).atZone(ZoneId.systemDefault())

        return AccountDomain(
            id = dto.id,
            name = dto.name,
            balance = dto.balance.toIntFromDecimal(),
            currency = dto.currency,
            incomeStats = mapStatItem(dto.incomeStats),
            expenseStats = mapStatItem(dto.expenseStats),
            createdAtDate = createdAt.toLocalDate(),
            createdAtTime = createdAt.toLocalTime().truncatedTo(ChronoUnit.MINUTES),
            updatedAtDate = updatedAt.toLocalDate(),
            updatedAtTime = updatedAt.toLocalTime().truncatedTo(ChronoUnit.MINUTES)
        )
    }

    private fun mapStatItem(dto: StatItemDTO): StatItemDomain {
        return StatItemDomain(
            categoryId = dto.categoryId,
            categoryName = dto.categoryName,
            emoji = dto.emoji,
            amount = dto.amount
        )
    }

    fun mapCategory(dto: CategoryDTO): CategoryDomain {
        return CategoryDomain(
            id = dto.id,
            name = dto.name,
            emoji = dto.emoji,
            isIncome = dto.isIncome
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun mapTransaction(dto: TransactionDTO): TransactionDomain {
        val transactionAt = Instant.parse(dto.transactionDate).atZone(ZoneId.systemDefault())

        return TransactionDomain(
            id = dto.id,
            account = mapAccountBrief(dto.account),
            category = mapCategory(dto.category),
            amount = dto.amount.toIntFromDecimal(),
            transactionDate = transactionAt.toLocalDate(),
            transactionTime = transactionAt.toLocalTime().truncatedTo(ChronoUnit.MINUTES),
            comment = dto.comment,
        )
    }

    private fun mapAccountBrief(dto: AccountBriefDTO): AccountBriefDomain {
        return AccountBriefDomain(
            id = dto.id,
            name = dto.name,
            balance = dto.balance.toIntFromDecimal(),
            currency = dto.currency
        )
    }
}

private fun String.toIntFromDecimal(): Int {
    return this.toBigDecimal()
        .setScale(0, RoundingMode.DOWN)
        .toInt()
}