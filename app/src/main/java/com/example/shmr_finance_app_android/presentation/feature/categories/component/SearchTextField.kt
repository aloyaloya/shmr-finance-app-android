package com.example.shmr_finance_app_android.presentation.feature.categories.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.shmr_finance_app_android.R

@Composable
fun SearchTextField(
    value: String,
    onChange: (String) -> Unit,
    onActionClick: () -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onChange,
        placeholder = {
            Text(
                text = stringResource(R.string.categories_search_placeholder),
                style = MaterialTheme.typography.bodyLarge
            )
        },
        trailingIcon = {
            IconButton(onClick = { onActionClick() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_search),
                    contentDescription = stringResource(
                        R.string.categories_search_description
                    )
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}