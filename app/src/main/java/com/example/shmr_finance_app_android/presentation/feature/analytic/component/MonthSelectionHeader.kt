package com.example.shmr_finance_app_android.presentation.feature.analytic.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R

@Composable
fun MonthSelectionHeader(
    startMonth: String,
    onStartMonth: () -> Unit,
    endMonth: String,
    onEndMonth: () -> Unit
) {
    MonthCard(
        modifier = Modifier.height(56.dp),
        label = stringResource(R.string.period_start),
        month = startMonth,
        onClick = onStartMonth
    )
    MonthCard(
        modifier = Modifier.height(56.dp),
        label = stringResource(R.string.period_end),
        month = endMonth,
        onClick = onEndMonth
    )
}

@Composable
private fun MonthCard(
    modifier: Modifier = Modifier,
    label: String,
    month: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clickable { onClick() }
            .padding(
                horizontal = dimensionResource(R.dimen.medium_padding),
                vertical = dimensionResource(R.dimen.small_padding)
            ),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.medium_spacer)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = CircleShape
                )
                .padding(
                    vertical = 6.dp,
                    horizontal = 16.dp
                ),
            text = month,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

    HorizontalDivider()
}