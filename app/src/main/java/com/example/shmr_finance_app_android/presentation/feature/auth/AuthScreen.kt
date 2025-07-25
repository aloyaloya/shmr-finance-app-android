package com.example.shmr_finance_app_android.presentation.feature.auth

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.core.di.daggerViewModel
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarBackAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig

@Composable
fun AuthScreen(
    viewModel: AuthViewModel = daggerViewModel(),
    onAuthenticated: () -> Unit,
    isChangePinMode: Boolean = false,
    updateConfigState: ((ScreenConfig) -> Unit)? = null
) {
    val authState by viewModel.authState.collectAsState()
    val pinInput by viewModel.pinInput.collectAsState()

    var animatedTextResId by remember { mutableStateOf(R.string.input_pin) }
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (isChangePinMode && updateConfigState != null) {
            viewModel.setChangePinMode(isChangePinMode)
            updateConfigState(
                ScreenConfig(
                    topBarConfig = TopBarConfig(
                        titleResId = R.string.pin_settings_screen_title,
                        backAction = TopBarBackAction(
                            actionUnit = onAuthenticated
                        )
                    )
                )
            )
        }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                onAuthenticated()
            }

            else -> {
                animatedTextResId = when (authState) {
                    AuthState.SetNewPin -> R.string.input_new_pin
                    AuthState.ConfirmNewPin -> R.string.confirm_new_pin
                    AuthState.VerifyPin -> R.string.input_pin
                    AuthState.Authenticated -> R.string.input_pin
                    is AuthState.Error -> (authState as AuthState.Error).messageResId
                    AuthState.Initial -> R.string.setup_new_pin
                    AuthState.VerifyCurrentPin -> R.string.input_pin
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(300)),
            exit = fadeOut(tween(300))
        ) {
            Text(
                text = stringResource(animatedTextResId),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(
                            if (index < pinInput.length) {
                                MaterialTheme.colorScheme.tertiary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        NumPad(
            onNumberClick = viewModel::onNumberEntered,
            onDeleteClick = viewModel::onDelete
        )
    }
}

@Composable
fun NumPad(
    onNumberClick: (Int) -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9)
        ).forEach { rowNumbers ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                rowNumbers.forEach { number ->
                    NumberButton(number = number, onClick = onNumberClick)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Box(modifier = Modifier.size(64.dp))
            NumberButton(number = 0, onClick = onNumberClick)
            DeleteButton(onClick = onDeleteClick)
        }
    }
}

@Composable
fun NumberButton(
    number: Int,
    onClick: (Int) -> Unit
) {
    Button(
        onClick = { onClick(number) },
        modifier = Modifier.size(64.dp),
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(
            text = number.toString(),
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Composable
fun DeleteButton(
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(64.dp),
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = MaterialTheme.colorScheme.tertiary
        )
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_cancel),
            contentDescription = stringResource(R.string.delete)
        )
    }
}
