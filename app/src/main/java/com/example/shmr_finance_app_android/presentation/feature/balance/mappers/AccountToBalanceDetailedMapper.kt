package com.example.shmr_finance_app_android.presentation.feature.balance.mappers

import com.example.shmr_finance_app_android.domain.model.AccountResponseDomain
import com.example.shmr_finance_app_android.presentation.feature.balance.models.BalanceDetailedUiModel
import javax.inject.Inject

/**
 * Маппер для преобразования [AccountResponseDomain] -> [BalanceDetailedUiModel]
 */
class AccountToBalanceDetailedMapper @Inject constructor() {
    fun map(domain: AccountResponseDomain): BalanceDetailedUiModel {
        return BalanceDetailedUiModel(
            id = domain.id,
            name = domain.name,
            amount = domain.balance.toString(),
            currencyCode = domain.currency,
            currencySymbol = domain.getCurrencySymbol(),
        )
    }
}