package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.data.remote.api.NetworkChecker
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для удаления транзакции по ID.
 * Поведение:
 * 1. При наличии сети делегирует запрос в [TransactionsRepository]
 */
@Reusable
class DeleteTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository,
    private val networkChecker: NetworkChecker
) {
    /**
     * Запускает UseCase через operator invoke().
     * @param transactionId ID удаляемой транзакции
     * @return [Result.success] с [Unit] при успехе,
     * [Result.failure] с:
     * [AppError.Network] - если нет интернета,
     * ошибками из [TransactionsRepository] - при проблемах запроса
     */
    suspend operator fun invoke(transactionId: Int): Result<Unit> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.deleteTransactionById(transactionId)
    }
}