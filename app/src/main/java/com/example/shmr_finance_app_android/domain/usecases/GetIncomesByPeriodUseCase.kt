package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для получения доходов за указанный период.
 * Особенности:
 * 1. Делегирует получение транзакций [GetTransactionsByPeriodUseCase]
 * 2. Фильтрует только доходы (где `category.isIncome == true`)
 * 3. Поддерживает опциональные даты периода
 * @see GetTransactionsByPeriodUseCase - базовый UseCase для получения транзакций
 */
@Reusable
class GetIncomesByPeriodUseCase @Inject constructor(
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase
) {
    /**
     * Запускает UseCase через operator invoke().
     *
     * @param accountId ID аккаунта
     * @param startDate дата начала периода необходимых транзакций в формате `yyyy-MM-dd`,
     * (по умолчанию - начало текущего месяца)
     * @param endDate дата конца периода необходимых транзакций в формате `yyyy-MM-dd`,
     * (по умолчанию - конец текущего месяца)
     *
     * @return [Result.success] с отфильтрованным списком доходов,
     * [Result.failure] с [AppError]
     */
    suspend operator fun invoke(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<TransactionResponseDomain>> {
        return getTransactionsByPeriod(accountId, startDate, endDate)
            .map { list ->
                list.filter { it.category.isIncome }
            }
    }
}