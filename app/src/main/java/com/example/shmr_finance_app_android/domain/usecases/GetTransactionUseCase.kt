package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.core.network.AppError
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для получения транзакции по ID.
 * Поведение:
 * 1. При наличии сети делегирует запрос в [TransactionsRepository]
 */
@Reusable
class GetTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    /**
     * Запускает UseCase через operator invoke().
     * @param transactionId ID получаемой транзакции
     * @return [Result.success] с [Unit] при успехе,
     * [Result.failure] с:
     * [AppError.Network] - если нет интернета,
     * ошибками из [TransactionsRepository] - при проблемах запроса
     */
    suspend operator fun invoke(transactionId: Int): Result<TransactionResponseDomain> {
        return repository.getTransactionById(transactionId)
    }
}