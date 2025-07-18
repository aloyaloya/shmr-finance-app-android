package com.example.shmr_finance_app_android.presentation.feature.transaction.mappers

import com.example.shmr_finance_app_android.domain.model.AccountResponseDomain
import com.example.shmr_finance_app_android.presentation.feature.transaction.models.BalanceBriefUiModel
import javax.inject.Inject

/**
 * Маппер для преобразования [AccountResponseDomain] -> [BalanceBriefUiModel]
 */
class AccountToBalanceBriefMapper @Inject constructor() {
    fun map(domain: AccountResponseDomain): BalanceBriefUiModel {
        return BalanceBriefUiModel(
            id = domain.id,
            name = domain.name,
            currencySymbol = domain.getCurrencySymbol()
        )
    }
}