package com.example.shmr_finance_app_android.presentation.feature.settings.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.shared.components.ListItemCard
import com.example.shmr_finance_app_android.presentation.shared.model.LeadContent
import com.example.shmr_finance_app_android.presentation.shared.model.ListItem
import com.example.shmr_finance_app_android.presentation.shared.model.MainContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SyncSliderSheet(
    modifier: Modifier = Modifier,
    currentMinutes: Int,
    onMinutesSelected: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    var sliderPosition by remember {
        mutableStateOf(roundToNearest15(currentMinutes).toFloat())
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        contentWindowInsets = { WindowInsets.systemBars.only(WindowInsetsSides.Bottom) }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.sync_frequency),
                style = MaterialTheme.typography.bodyLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacer)))


            Slider(
                value = sliderPosition,
                onValueChange = { newValue ->
                    sliderPosition = roundToNearest15(newValue.toInt()).toFloat()
                },
                onValueChangeFinished = {
                    onMinutesSelected(sliderPosition.toInt())
                },
                valueRange = 15f..240f,
                steps = (240 / 15) - 2,
                colors = SliderDefaults.colors(
                    activeTrackColor = MaterialTheme.colorScheme.tertiary,
                    inactiveTrackColor = MaterialTheme.colorScheme.onTertiaryContainer,
                    inactiveTickColor = MaterialTheme.colorScheme.surface,
                    thumbColor = MaterialTheme.colorScheme.tertiary
                ),
                modifier = Modifier.padding(
                    horizontal = dimensionResource(R.dimen.medium_padding)
                )
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.small_spacer)))
            Text(
                text = "${sliderPosition.toInt()} ${stringResource(R.string.minutes)}",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.large_spacer)))
            DismissButton(
                onDismiss = {
                    scope.launch { sheetState.hide() }.invokeOnCompletion { onDismiss() }
                }
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

private fun roundToNearest15(value: Int): Int {
    return ((value + 7) / 15) * 15
}