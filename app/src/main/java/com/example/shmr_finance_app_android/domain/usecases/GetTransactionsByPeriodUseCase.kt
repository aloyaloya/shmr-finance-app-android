package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.core.network.AppError
import com.example.shmr_finance_app_android.core.network.NetworkChecker
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для получения транзакций за период.
 * Особенности:
 * 1. Проверяет интернет-соединение через [NetworkChecker]
 * 2. Если сети нет - сразу возвращает [Result.failure] с [AppError.Network]
 * 3. При наличии сети делегирует запрос в [TransactionsRepository]
 */
@Reusable
class GetTransactionsByPeriodUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val networkChecker: NetworkChecker
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
     * @return [Result.success] с отфильтрованным по дате (новые -> старые) списком транзакций,
     * [Result.failure] с [AppError]
     */
    suspend operator fun invoke(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<TransactionResponseDomain>> {
        if (networkChecker.isNetworkAvailable()) {
            repository.syncTransactions()
        }

        return repository.getTransactionsByPeriod(accountId, startDate, endDate)
            .map { list ->
                list.sortedByDescending { it.transactionDate }
            }
    }
}