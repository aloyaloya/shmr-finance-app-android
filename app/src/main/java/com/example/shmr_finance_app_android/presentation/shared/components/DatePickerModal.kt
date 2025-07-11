package com.example.shmr_finance_app_android.presentation.shared.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let {
                        onDateSelected(it)
                    }
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.tertiary,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text(
                    text = stringResource(R.string.select),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        },
        colors = DatePickerDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    modifier = Modifier.padding(16.dp),
                    text = stringResource(R.string.choice_date)
                )
            },
            colors = DatePickerDefaults.colors(
                todayDateBorderColor = MaterialTheme.colorScheme.tertiary,
                todayContentColor = MaterialTheme.colorScheme.tertiary,
                selectedDayContainerColor = MaterialTheme.colorScheme.tertiary,
                selectedDayContentColor = MaterialTheme.colorScheme.background
            )
        )
    }
}