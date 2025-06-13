package com.example.shmr_finance_app_android.ui.viewmodels

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.navigation.RootScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TopBarViewModel : ViewModel() {
    private val _state = MutableStateFlow(TopBarState(R.string.expense_screen_title))
    val state: StateFlow<TopBarState> = _state

    fun updateStateForScreen(screen: RootScreen) {
        _state.value = TopBarState(
            title = screen.title,
            showBackButton = screen.showBackButton,
            actionIcon = screen.actionIcon,
            actionDescription = screen.actionDescription,
            onActionRoute = screen.onActionRoute
        )
    }
}

data class TopBarState(
    @StringRes val title: Int,
    val showBackButton: Boolean = false,
    @DrawableRes val actionIcon: Int? = null,
    @StringRes val actionDescription: Int? = null,
    val onActionRoute: String? = null
)