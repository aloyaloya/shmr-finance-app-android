package com.example.shmr_finance_app_android.ui.screens.settings_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R

@Composable
fun SettingsOptionCard(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .height(56.dp)
            .clickable { onClick() }
            .padding(
                horizontal = dimensionResource(R.dimen.medium_padding),
                vertical = dimensionResource(R.dimen.small_padding)
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.medium_spacer)
        )
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
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

    HorizontalDivider()
}