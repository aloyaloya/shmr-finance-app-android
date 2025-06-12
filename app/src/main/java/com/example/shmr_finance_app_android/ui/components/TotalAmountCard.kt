package com.example.shmr_finance_app_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.example.shmr_finance_app_android.R

/**
 * Карточка заголовка страницы с общим расходом/доходом
 *
 * @param totalAmount Общая сумма
 */
@Composable
fun TotalAmountCard(
    modifier: Modifier = Modifier,
    totalAmount: String
) {
    Row(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
            .padding(all = dimensionResource(R.dimen.medium_padding)),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.medium_spacer)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.total_amount),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = totalAmount,
            style = MaterialTheme.typography.bodyLarge
        )
    }

    HorizontalDivider()
}