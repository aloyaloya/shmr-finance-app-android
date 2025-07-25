package com.example.shmr_finance_app_android.presentation.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.utils.formatTimestamp
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutSheet(
    modifier: Modifier = Modifier,
    version: String,
    lastUpdate: Long,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        contentWindowInsets = { WindowInsets.systemBars.only(WindowInsetsSides.Bottom) }
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.medium_padding)),
            text = stringResource(R.string.application_version, version),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.medium_spacer)))
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(R.dimen.medium_padding)),
            text = stringResource(R.string.last_update, formatTimestamp(lastUpdate)),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_spacer)))
        DismissButton(
            onDismiss = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    onDismiss()
                }
            }
        )
    }
}

@Composable
private fun DismissButton(
    onDismiss: () -> Unit
) {
    ListItemCard(
        modifier = Modifier
            .height(72.dp)
            .background(color = MaterialTheme.colorScheme.errorContainer)
            .clickable(onClick = onDismiss),
        item = ListItem(
            lead = LeadContent.Icon(
                iconResId = R.drawable.ic_dismiss,
                color = MaterialTheme.colorScheme.onErrorContainer
            ),
            content = MainContent(
                title = stringResource(R.string.dismiss),
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        )
    )
}