package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.core.network.AppError
import com.example.shmr_finance_app_android.core.utils.combineDateTimeToIso
import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.model.TransactionResponseDomain
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для создания новой транзакции.
 * Поведение:
 * 1. При наличии сети делегирует запрос в [TransactionsRepository]
 */
@Reusable
class CreateTransactionUseCase @Inject constructor(
    private val repository: TransactionsRepository
) {
    /**
     * Запускает UseCase через operator invoke().
     * @param accountId ID аккаунта
     * @param categoryId ID категории
     * @param amount Размер расхода/дохода транзакции
     * @param transactionDate Дата транзакции
     * @param transactionTime Время транзакции
     * @param comment Комментарий транзакции
     * @return [Result.success] с [TransactionResponseDomain] при успехе,
     * [Result.failure] с:
     * [AppError.Network] - если нет интернета,
     * ошибками из [TransactionsRepository] - при проблемах запроса
     */
    suspend operator fun invoke(
        accountId: Int,
        categoryId: Int,
        amount: String,
        transactionDate: String,
        transactionTime: String,
        comment: String
    ): Result<TransactionDomain> {
        return repository.createTransaction(
            accountId,
            categoryId,
            amount,
            combineDateTimeToIso(transactionDate, transactionTime),
            comment
        )
    }
}