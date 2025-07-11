package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.data.remote.api.AppError
import com.example.shmr_finance_app_android.data.remote.api.NetworkChecker
import com.example.shmr_finance_app_android.domain.model.AccountResponseDomain
import com.example.shmr_finance_app_android.domain.repository.AccountRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для получения данных аккаунта по ID.
 * Поведение:
 * 1. Проверяет интернет-соединение через [NetworkChecker]
 * 2. Если сети нет - сразу возвращает [Result.failure] с [AppError.Network]
 * 3. При наличии сети делегирует запрос в [AccountRepository]
 */
@Reusable
class GetAccountUseCase @Inject constructor(
    private val repository: AccountRepository,
    private val networkChecker: NetworkChecker
) {
    /**
     * Запускает UseCase через operator invoke().
     * @param accountId ID аккаунта
     * @return [Result.success] с [AccountResponseDomain] при успехе,
     * [Result.failure] с:
     * [AppError.Network] - если нет интернета,
     * ошибками из [AccountRepository] - при проблемах запроса
     */
    suspend operator fun invoke(accountId: Int): Result<AccountResponseDomain> {
        if (!networkChecker.isNetworkAvailable()) {
            return Result.failure(AppError.Network)
        }

        return repository.getAccountById(accountId)
    }
}