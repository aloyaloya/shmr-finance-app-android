package com.example.shmr_finance_app_android.data.repository.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.TransactionDTO
import com.example.shmr_finance_app_android.data.model.TransactionResponseDTO
import com.example.shmr_finance_app_android.domain.model.AccountBriefDomain
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Маппер для преобразования [TransactionResponseDTO] -> [TransactionResponseDomain].
 * Делегирует маппинг категории к [CategoryDomainMapper]
 * Создает краткую версию данных аккаунта [AccountBriefDomain]
 */
internal class TransactionsDomainMapper @Inject constructor(
    private val categoryMapper: CategoryDomainMapper
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun mapTransactionResponse(dto: TransactionResponseDTO): TransactionResponseDomain {
        val transactionAt = Instant.parse(dto.transactionDate).atZone(ZoneId.systemDefault())

        return TransactionResponseDomain(
            id = dto.id,
            account = mapAccountBrief(dto.account),
            category = categoryMapper.mapCategory(dto.category),
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

    fun mapTransaction(dto: TransactionDTO): TransactionDomain {
        return TransactionDomain(
            id = dto.id,
            accountId = dto.accountId,
            categoryId = dto.id,
            amount = dto.amount,
            transactionDate = dto.transactionDate,
            comment = dto.comment,
            createdAt = dto.createdAt,
            updatedAt = dto.updatedAt
        )
    }
}

/**
 * Преобразует строку с десятичным числом в целое число [Int].
 * Дробная часть отбрасывается без округления
 **/
private fun String.toIntFromDecimal(): Int {
    return this.toBigDecimal()
        .setScale(0, RoundingMode.DOWN)
        .toInt()
}