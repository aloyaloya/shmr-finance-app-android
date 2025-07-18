package com.example.shmr_finance_app_android.presentation.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.shmr_finance_app_android.presentation.feature.categories.model.CategoryUiModel
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelectionSheet(
    modifier: Modifier = Modifier,
    items: List<CategoryUiModel>,
    onItemSelected: (CategoryUiModel) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        contentWindowInsets = { WindowInsets.systemBars.only(WindowInsetsSides.Bottom) }
    ) {
        ItemsListContent(
            modifier = Modifier
                .weight(1f, fill = false)
                .heightIn(max = 600.dp),
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
    modifier: Modifier = Modifier,
    items: List<CategoryUiModel>,
    onItemSelected: (CategoryUiModel) -> Unit
) {
    LazyColumn(modifier = modifier) {
        items(items, key = { category -> category.id }) { category ->
            ListItemCard(
                modifier = Modifier
                    .height(72.dp)
                    .clickable { onItemSelected(category) },
                item = category.toListItem()
            )
        }
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