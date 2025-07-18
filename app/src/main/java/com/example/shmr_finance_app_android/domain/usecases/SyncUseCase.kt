package com.example.shmr_finance_app_android.domain.usecases

import com.example.shmr_finance_app_android.domain.repository.AccountRepository
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import javax.inject.Inject

class SyncUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val categoryRepository: CategoriesRepository,
    private val transactionRepository: TransactionsRepository
) {
    suspend fun syncAllData() {
        accountRepository.syncAccounts()
        categoryRepository.syncCategories()
        transactionRepository.syncTransactions()
    }
}