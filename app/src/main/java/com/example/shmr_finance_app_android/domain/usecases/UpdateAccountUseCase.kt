package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.data.remote.api.NetworkChecker
import com.example.shmr_finance_app_android.domain.model.AccountBriefDomain
import com.example.shmr_finance_app_android.domain.repository.AccountRepository
import dagger.Reusable
import javax.inject.Inject

/**
 * UseCase для обновления данных аккаунта по ID.
 * Поведение:
 * 1. При наличии сети делегирует запрос в [AccountRepository]
 */
@Reusable
class UpdateAccountUseCase @Inject constructor(
    private val repository: AccountRepository,
    private val networkChecker: NetworkChecker
) {
    /**
     * Запускает UseCase через operator invoke().
     * @param accountId ID аккаунта
     * @param accountName Название счета
     * @param accountBalance Баланс счета
     * @param accountCurrency Валюта счета
     */
    suspend operator fun invoke(
        accountId: Int,
        accountName: String,
        accountBalance: Int,
        accountCurrency: String
    ) {
        if (networkChecker.isNetworkAvailable()) {
            repository.updateAccountById(
                AccountBriefDomain(
                    id = accountId,
                    name = accountName,
                    balance = accountBalance,
                    currency = accountCurrency
                )
            )
        }
    }
}