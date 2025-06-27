package com.example.shmr_finance_app_android.presentation.shared.components

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

/**
 * Карточка элемента списка с ведущим, основным и завершающим контентом.
 *
 * @param modifier Модификатор
 * @param item Данные для отображения
 * @param trailIcon Иконка в trailing-контенте (опционально)
 * @param showDivider Отображается ли HorizontalDivider (по умолчанию да)
 * @param subtitleStyle Стиль subtitle текст в main-контенте
 */
@Composable
fun ListItemCard(
    modifier: Modifier = Modifier,
    item: ListItem,
    trailIcon: Int? = null,
    showDivider: Boolean = true,
    subtitleStyle: TextStyle = MaterialTheme.typography.bodyMedium
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
            content = item.content,
            subtitleStyle = subtitleStyle
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

    if (showDivider) {
        HorizontalDivider()
    }
}

@Composable
private fun TrailItemContent(
    modifier: Modifier = Modifier,
    trail: TrailContent
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End
    ) {
        Text(
            text = trail.text,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        trail.subtext?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun MainItemContent(
    modifier: Modifier = Modifier,
    content: MainContent,
    subtitleStyle: TextStyle
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

        content.subtitle?.takeIf { it.isNotEmpty() }?.let {
            Text(
                text = it,
                style = subtitleStyle,
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