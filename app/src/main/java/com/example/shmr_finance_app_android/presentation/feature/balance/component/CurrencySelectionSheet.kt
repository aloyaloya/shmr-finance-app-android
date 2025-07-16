package com.example.shmr_finance_app_android.presentation.feature.balance.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.feature.balance.models.CurrencyItem
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencySelectionSheet(
    modifier: Modifier = Modifier,
    items: List<CurrencyItem>,
    onItemSelected: (CurrencyItem) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        sheetState = sheetState,
        windowInsets = WindowInsets.systemBars.only(WindowInsetsSides.Bottom)
    ) {
        ItemsListContent(
            items = items,
            onItemSelected = {
                onItemSelected(it)
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    onDismiss()
                }
            }
        )
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
private fun ItemsListContent(
    items: List<CurrencyItem>,
    onItemSelected: (CurrencyItem) -> Unit
) {
    items.forEach { item ->
        val itemTitle = stringResource(item.currencyResId)
        ListItemCard(
            modifier = Modifier
                .height(72.dp)
                .clickable { onItemSelected(item) },
            item = ListItem(
                lead = LeadContent.Icon(item.currencyIconResId),
                content = MainContent(title = "$itemTitle ${item.currencySymbol}")
            )
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