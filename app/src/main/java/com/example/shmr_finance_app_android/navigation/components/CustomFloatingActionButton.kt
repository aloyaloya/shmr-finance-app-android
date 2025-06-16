package com.example.shmr_finance_app_android.navigation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun CustomFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    description: Int
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        contentColor = MaterialTheme.colorScheme.surface,
        containerColor = MaterialTheme.colorScheme.tertiary,
        shape = CircleShape,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(description)
        )
    }
}