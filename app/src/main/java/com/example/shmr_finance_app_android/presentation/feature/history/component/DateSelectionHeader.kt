package com.example.shmr_finance_app_android.presentation.feature.history.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import com.example.shmr_finance_app_android.presentation.shared.model.TrailContent

@Composable
fun DateSelectionHeader(
    startDate: String,
    onStartDate: () -> Unit,
    endDate: String,
    onEndDate: () -> Unit
) {
    ListItemCard(
        modifier = Modifier
            .clickable { onStartDate() }
            .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
            .height(56.dp),
        item = ListItem(
            content = MainContent(title = stringResource(R.string.period_start)),
            trail = TrailContent(text = startDate)
        )
    )
    ListItemCard(
        modifier = Modifier
            .clickable { onEndDate() }
            .background(color = MaterialTheme.colorScheme.onTertiaryContainer)
            .height(56.dp),
        item = ListItem(
            content = MainContent(title = stringResource(R.string.period_end)),
            trail = TrailContent(text = endDate)
        )
    )
}