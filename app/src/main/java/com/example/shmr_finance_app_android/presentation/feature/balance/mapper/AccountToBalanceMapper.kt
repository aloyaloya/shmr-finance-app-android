package com.example.shmr_finance_app_android.presentation.feature.balance.mapper

import com.example.shmr_finance_app_android.domain.model.AccountDomain
import com.example.shmr_finance_app_android.presentation.feature.balance.model.BalanceUiModel
import javax.inject.Inject

/**
 * Маппер для преобразования [AccountDomain] -> [BalanceUiModel]
 */
class AccountToBalanceMapper @Inject constructor() {
    fun map(domain: AccountDomain): BalanceUiModel {
        return BalanceUiModel(
            id = domain.id,
            amount = domain.balance,
            currency = domain.getCurrencySymbol()
        )
    }
}