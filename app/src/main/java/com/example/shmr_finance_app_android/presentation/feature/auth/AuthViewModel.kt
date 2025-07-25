package com.example.shmr_finance_app_android.presentation.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.domain.repository.PinRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    data object Initial : AuthState()
    data object VerifyCurrentPin : AuthState()
    data object SetNewPin : AuthState()
    data object ConfirmNewPin : AuthState()
    data object VerifyPin : AuthState()
    data object Authenticated : AuthState()
    data class Error(val messageResId: Int) : AuthState()
}

class AuthViewModel @Inject constructor(
    private val pinRepository: PinRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Initial)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _pinInput = MutableStateFlow("")
    val pinInput: StateFlow<String> = _pinInput.asStateFlow()

    private var newPin: String = ""
    private var isChangePinMode: Boolean = false

    val hasExistingPin: Boolean get() = pinRepository.hasPin()
    val isConfirmingPin: Boolean get() = newPin.isNotEmpty()

    init {
        reset()
    }

    fun setChangePinMode(enabled: Boolean) {
        isChangePinMode = enabled
        reset()
    }

    fun onNumberEntered(number: Int) {
        if (_pinInput.value.length >= 4) return

        viewModelScope.launch {
            _pinInput.value += number.toString()

            if (_authState.value is AuthState.Error) {
                _authState.value = when {
                    isChangePinMode && hasExistingPin -> AuthState.VerifyCurrentPin
                    hasExistingPin -> AuthState.VerifyPin
                    newPin.isEmpty() -> AuthState.SetNewPin
                    else -> AuthState.ConfirmNewPin
                }
            }

            if (_pinInput.value.length == 4) {
                processPin(_pinInput.value)
            }
        }
    }

    fun onDelete() {
        if (_pinInput.value.isNotEmpty()) {
            _pinInput.value = _pinInput.value.dropLast(1)
        }
    }

    private fun processPin(pin: String) {
        when (_authState.value) {
            AuthState.VerifyCurrentPin -> {
                if (pinRepository.checkPin(pin)) {
                    _pinInput.value = ""
                    _authState.value = AuthState.SetNewPin
                } else {
                    _authState.value = AuthState.Error(R.string.invalid_pin_error)
                    _pinInput.value = ""
                }
            }

            AuthState.SetNewPin -> {
                newPin = pin
                _pinInput.value = ""
                _authState.value = AuthState.ConfirmNewPin
            }

            AuthState.ConfirmNewPin -> {
                if (pin == newPin) {
                    pinRepository.savePin(pin)
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(R.string.pin_mismatch_error)
                    reset()
                }
            }

            AuthState.VerifyPin -> {
                if (pinRepository.checkPin(pin)) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(R.string.invalid_pin_error)
                    _pinInput.value = ""
                }
            }

            else -> Unit
        }
    }

    fun reset() {
        newPin = ""
        _pinInput.value = ""
        _authState.value = when {
            isChangePinMode && hasExistingPin -> AuthState.VerifyCurrentPin
            hasExistingPin -> AuthState.VerifyPin
            else -> AuthState.SetNewPin
        }
    }
}