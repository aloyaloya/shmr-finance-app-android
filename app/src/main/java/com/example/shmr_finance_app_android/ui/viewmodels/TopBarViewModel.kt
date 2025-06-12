package com.example.shmr_finance_app_android.ui.viewmodels

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopBarViewModel : ViewModel() {
    private val _state = MutableStateFlow(TopBarState(""))
    val state: StateFlow<TopBarState> = _state

    fun update(state: TopBarState) {
        _state.value = state
    }
}

data class TopBarState(
    val title: String,
    val showBackButton: Boolean = false,
    @DrawableRes val actionIcon: Int? = null,
    val actionDescription: String? = null,
    val onActionClick: (() -> Unit)? = null
)