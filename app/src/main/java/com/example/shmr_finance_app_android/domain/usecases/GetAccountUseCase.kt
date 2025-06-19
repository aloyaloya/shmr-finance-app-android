package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.domain.repository.FinanceRepository
import javax.inject.Inject

class GetAccountUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(accountId: Int): AccountDomain {
        return repository.getAccountById(accountId)
    }
}