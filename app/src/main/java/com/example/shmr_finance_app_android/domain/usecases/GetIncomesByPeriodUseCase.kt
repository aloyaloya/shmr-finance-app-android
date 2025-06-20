package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.domain.model.TransactionDomain
import dagger.Reusable
import javax.inject.Inject

@Reusable
class GetIncomesByPeriodUseCase @Inject constructor(
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase
) {
    suspend operator fun invoke(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): Result<List<TransactionDomain>> {
        return getTransactionsByPeriod(accountId, startDate, endDate)
            .map { list ->
                list.filter { it.category.isIncome }
            }
    }
}