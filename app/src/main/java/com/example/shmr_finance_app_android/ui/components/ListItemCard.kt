package com.example.shmr_finance_app_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.text.style.TextOverflow
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.data.model.ui.LeadContent
import com.example.shmr_finance_app_android.data.model.ui.ListItem
import com.example.shmr_finance_app_android.data.model.ui.MainContent
import com.example.shmr_finance_app_android.data.model.ui.TrailContent

/**
 * Карточка элемента списка с ведущим, основным и завершающим контентом.
 *
 * @param item Данные для отображения
 * @param trailIcon Иконка в trailing-контенте (опционально)
 */
@Composable
fun ListItemCard(
    modifier: Modifier = Modifier,
    item: ListItem,
    trailIcon: Int? = null
) {
    Row(
        modifier = modifier.padding(
            horizontal = dimensionResource(R.dimen.medium_padding)
        ),
        horizontalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.medium_spacer)
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item.lead?.let { lead ->
            LeadItemContent(lead = lead)
        }

        MainItemContent(
            modifier = Modifier.weight(1f),
            content = item.content
        )

        item.trail?.let { trail ->
            TrailItemContent(trail = trail)
        }

        trailIcon?.let {
            Icon(
                modifier = Modifier
                    .width(dimensionResource(R.dimen.large_icon_size))
                    .align(Alignment.CenterVertically),
                painter = painterResource(trailIcon),
                contentDescription = stringResource(R.string.trail_icon_description)
            )
        }
    }

    HorizontalDivider()
}

@Composable
private fun TrailItemContent(
    modifier: Modifier = Modifier,
    trail: TrailContent
) {
    Text(
        modifier = modifier,
        text = trail.text,
        style = MaterialTheme.typography.bodyLarge
    )
}

@Composable
private fun MainItemContent(
    modifier: Modifier = Modifier,
    content: MainContent
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = content.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        content.subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun LeadItemContent(
    modifier: Modifier = Modifier,
    lead: LeadContent
) {
    Box(
        modifier = modifier
            .size(dimensionResource(R.dimen.large_icon_size))
            .background(
                color = lead.color ?: MaterialTheme.colorScheme.onTertiaryContainer,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = lead.text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}