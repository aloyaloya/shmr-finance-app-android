package com.example.shmr_finance_app_android.presentation.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R
import kotlinx.coroutines.delay

@Composable
fun AnimatedSnackbar(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    messageResId: Int,
    onDismiss: () -> Unit,
    durationMillis: Long = 3000L,
    backgroundColor: Color = MaterialTheme.colorScheme.errorContainer,
    textColor: Color = MaterialTheme.colorScheme.onErrorContainer,
    showDismissButton: Boolean = true
) {
    LaunchedEffect(isVisible) {
        if (isVisible) {
            delay(durationMillis)
            onDismiss()
        }
    }

    AnimatedVisibility(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopCenter)
            .padding(
                vertical = dimensionResource(R.dimen.small_padding),
                horizontal = dimensionResource(R.dimen.small_padding)
            )
            .height(52.dp),
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(100)) +
                scaleIn(
                    initialScale = 0.5f,
                    animationSpec = tween(300)
                ),
        exit = fadeOut(animationSpec = tween(100)) +
                scaleOut(
                    targetScale = 0.5f,
                    animationSpec = tween(300)
                )
    ) {
        Surface(
            shape = MaterialTheme.shapes.small,
            color = backgroundColor,
            shadowElevation = 6.dp,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = dimensionResource(R.dimen.medium_padding)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = stringResource(messageResId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = textColor
                )
                if (showDismissButton) {
                    IconButton(onClick = onDismiss) {
                        Icon(
                            painter = painterResource(R.drawable.ic_cancel),
                            contentDescription = stringResource(R.string.close),
                            tint = textColor
                        )
                    }
                }
            }
        }
    }
}