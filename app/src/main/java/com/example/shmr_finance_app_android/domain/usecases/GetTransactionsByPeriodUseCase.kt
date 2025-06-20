package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import com.example.shmr_finance_app_android.domain.repository.FinanceRepository
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetTransactionsByPeriodUseCase @Inject constructor(
    private val repository: FinanceRepository
) {
    suspend operator fun invoke(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<TransactionDomain>> {
        return repository.getTransactionsByPeriod(accountId, startDate, endDate)
            .map { list ->
                list.sortedByDescending { it.transactionDate }
            }
    }
}