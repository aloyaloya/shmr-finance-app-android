package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.domain.model.TransactionDomain

class GetExpensesTransactionsByPeriodUseCase(
    private val getTransactionsByPeriod: GetTransactionsByPeriodUseCase
) {
    suspend operator fun invoke(
        accountId: Int,
        startDate: String? = null,
        endDate: String? = null
    ): List<TransactionDomain> {
        return getTransactionsByPeriod(
            accountId,
            startDate,
            endDate
        ).filterNot { it.category.isIncome }
    }
}