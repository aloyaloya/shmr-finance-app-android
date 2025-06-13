package com.example.shmr_finance_app_android.ui.screens.balance_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.shmr_finance_app_android.R

@Composable
fun BalanceAmountCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    balanceAmount: String,
    emoji: String
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
            .padding(all = dimensionResource(R.dimen.medium_padding)),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.medium_spacer)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = modifier
                .size(dimensionResource(R.dimen.large_icon_size))
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(R.string.balance),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = balanceAmount,
            style = MaterialTheme.typography.bodyLarge
        )
        Icon(
            modifier = Modifier
                .width(dimensionResource(R.dimen.medium_icon_size))
                .align(Alignment.CenterVertically),
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = stringResource(R.string.trail_icon_description)
        )
    }
}