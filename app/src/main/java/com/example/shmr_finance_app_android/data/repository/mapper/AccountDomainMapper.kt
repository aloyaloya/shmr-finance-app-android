package com.example.shmr_finance_app_android.data.repository.mapper

import com.example.shmr_finance_app_android.data.model.AccountBriefDTO
import com.example.shmr_finance_app_android.data.model.AccountDTO
import com.example.shmr_finance_app_android.data.model.AccountResponseDTO
import com.example.shmr_finance_app_android.data.model.StatItemDTO
import com.example.shmr_finance_app_android.domain.model.AccountBriefDomain
import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.model.AccountResponseDomain
import com.example.shmr_finance_app_android.domain.model.StatItemDomain
import java.math.RoundingMode
import java.time.Instant
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Маппер для:
 * Преобразования [AccountResponseDTO] -> [AccountResponseDomain] при получении данных
 * Преобразования [AccountBriefDomain] -> [AccountBriefDTO] при изменении данных
 * Создает модель данных статистики аккаунта [StatItemDomain]
 */
internal class AccountDomainMapper @Inject constructor() {
    fun mapAccountResponse(dto: AccountResponseDTO): AccountResponseDomain {
        val createdAt = Instant.parse(dto.createdAt).atZone(ZoneId.systemDefault())
        val updatedAt = Instant.parse(dto.updatedAt).atZone(ZoneId.systemDefault())

        return AccountResponseDomain(
            id = dto.id,
            name = dto.name,
            balance = dto.balance.toIntFromDecimal(),
            currency = dto.currency,
            incomeStats = dto.incomeStats.map { mapStatItem(it) },
            expenseStats = dto.expenseStats.map { mapStatItem(it) },
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

    fun mapAccount(account: AccountDTO): AccountDomain {
        return AccountDomain(
            id = account.id,
            userId = account.userId,
            name = account.name,
            balance = account.balance,
            currency = account.currency,
            createdAt = account.createdAt,
            updatedAt = account.updatedAt
        )
    }

    fun mapAccountBrief(domain: AccountBriefDomain): AccountBriefDTO {
        return AccountBriefDTO(
            id = domain.id,
            name = domain.name,
            balance = domain.balance.toString(),
            currency = domain.currency
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