package com.example.shmr_finance_app_android.presentation.feature.categories.component

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.example.shmr_finance_app_android.R

@Composable
fun SearchTextField(
    value: String,
    onChange: (String) -> Unit,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                if (!focusState.isFocused) {
                    keyboardController?.hide()
                }
            },
        value = value,
        onValueChange = onChange,
        singleLine = true,
        placeholder = {
            Text(
                text = stringResource(R.string.categories_search_placeholder),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingIcon = {
            AnimatedActionButton(
                isEmpty = value.isNotEmpty(),
                onClearClick = {
                    onChange("")
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus()
                keyboardController?.hide()
                onActionClick()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        )
    )
}

@Composable
private fun AnimatedActionButton(
    isEmpty: Boolean,
    onClearClick: () -> Unit
) {
    val transition = updateTransition(targetState = isEmpty)

    val searchAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "searchAlpha"
    ) { hasText -> if (hasText) 0f else 1f }

    val searchScale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "searchScale"
    ) { hasText -> if (hasText) 0.7f else 1f }

    val clearAlpha by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "clearAlpha"
    ) { hasText -> if (hasText) 1f else 0f }

    val clearScale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200) },
        label = "clearScale"
    ) { hasText -> if (hasText) 1f else 0.7f }

    Box(contentAlignment = Alignment.Center) {
        Icon(
            modifier = Modifier
                .graphicsLayer {
                    alpha = searchAlpha
                    scaleX = searchScale
                    scaleY = searchScale
                },
            painter = painterResource(R.drawable.ic_search),
            contentDescription = stringResource(R.string.categories_search_description)
        )

        IconButton(
            onClick = onClearClick,
            modifier = Modifier
                .graphicsLayer {
                    alpha = clearAlpha
                    scaleX = clearScale
                    scaleY = clearScale
                }
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "f"
            )
        }
    }
}