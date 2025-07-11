package com.example.shmr_finance_app_android.presentation.feature.balance.mapper

import com.example.shmr_finance_app_android.domain.model.AccountResponseDomain
import com.example.shmr_finance_app_android.presentation.feature.balance.model.BalanceUiModel
import javax.inject.Inject

/**
 * Маппер для преобразования [AccountResponseDomain] -> [BalanceUiModel]
 */
class AccountToBalanceMapper @Inject constructor() {
    fun map(domain: AccountResponseDomain): BalanceUiModel {
        return BalanceUiModel(
            id = domain.id,
            amount = domain.balance,
            currency = domain.getCurrencySymbol()
        )
    }
}