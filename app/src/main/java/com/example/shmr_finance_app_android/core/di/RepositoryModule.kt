package com.example.shmr_finance_app_android.core.di

import com.example.shmr_finance_app_android.data.repository.AccountRepositoryImpl
import com.example.shmr_finance_app_android.data.repository.CategoriesRepositoryImpl
import com.example.shmr_finance_app_android.data.repository.TransactionsRepositoryImpl
import com.example.shmr_finance_app_android.domain.repository.AccountRepository
import com.example.shmr_finance_app_android.domain.repository.CategoriesRepository
import com.example.shmr_finance_app_android.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

/**
 * Отвечает за предоставление реализаций репозиториев
 * в виде абстракций (интерфейсов) для всего приложения.
 */
@Module
internal interface RepositoryModule {

    /**
     * Предоставляет реализацию [AccountRepository] для работы с репозиторием аккаунтов.
     * Заменяет абстракцию конкретным классом [AccountRepositoryImpl].
     */
    @Binds
    @Singleton
    fun bindAccountRepository(
        accountRepository: AccountRepositoryImpl,
    ): AccountRepository

    /**
     * Предоставляет реализацию [CategoriesRepository] для работы с репозиторием категорий.
     * Заменяет абстракцию конкретным классом [CategoriesRepositoryImpl].
     */
    @Binds
    @Singleton
    fun bindCategoriesRepository(
        categoriesRepository: CategoriesRepositoryImpl,
    ): CategoriesRepository

    /**
     * Предоставляет реализацию [TransactionsRepository] для работы с репозиторием транзакций.
     * Заменяет абстракцию конкретным классом [TransactionsRepositoryImpl].
     */
    @Binds
    @Singleton
    fun bindTransactionsRepository(
        transactionsRepository: TransactionsRepositoryImpl,
    ): TransactionsRepository
}