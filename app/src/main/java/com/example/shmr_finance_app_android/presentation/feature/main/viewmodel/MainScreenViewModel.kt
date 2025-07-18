package com.example.shmr_finance_app_android.presentation.feature.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.shmr_finance_app_android.R
import com.example.shmr_finance_app_android.presentation.feature.main.model.FloatingActionConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.ScreenConfig
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarAction
import com.example.shmr_finance_app_android.presentation.feature.main.model.TopBarConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel главного экрана, отвечающая за:
 * - Управление конфигурацией UI (TopAppBar, FAB) для текущего экрана
 * - Централизованное обновление состояния навигации
 */
class MainScreenViewModel @Inject constructor(
    private val workManager: WorkManager
) : ViewModel() {

    private val _configState = MutableStateFlow(
        ScreenConfig(
            topBarConfig = TopBarConfig(
                titleResId = R.string.expense_screen_title,
                action = TopBarAction(
                    iconResId = R.drawable.ic_history,
                    descriptionResId = R.string.expenses_history_description,
                    actionUnit = {}
                )
            ),
            floatingActionConfig = FloatingActionConfig(
                descriptionResId = R.string.add_expense_description,
                actionUnit = {}
            )
        )
    )

    val configState: StateFlow<ScreenConfig> = _configState

    private val _events = MutableSharedFlow<MainScreenEvent>()
    val events: SharedFlow<MainScreenEvent> = _events

    private var lastSyncState: WorkInfo.State? = null

    init {
        checkCurrentWorkState()
    }

    private fun checkCurrentWorkState() {
        workManager
            .getWorkInfosForUniqueWorkLiveData("sync_work")
            .asFlow()
            .onEach { workInfos ->
                val info = workInfos.firstOrNull() ?: return@onEach
                val currentState = info.state

                if (currentState != lastSyncState) {
                    lastSyncState = currentState

                    val event = when (currentState) {
                        WorkInfo.State.ENQUEUED, WorkInfo.State.RUNNING ->
                            MainScreenEvent.ShowDefaultSnackBar(R.string.sync_data_started)

                        WorkInfo.State.SUCCEEDED ->
                            MainScreenEvent.ShowDefaultSnackBar(R.string.sync_data_success)

                        WorkInfo.State.FAILED ->
                            MainScreenEvent.ShowErrorSnackBar(R.string.sync_data_failed)

                        else -> MainScreenEvent.ShowErrorSnackBar(R.string.sync_data_failed)
                    }

                    emitEvent(event)
                }
            }
            .catch { throwable ->
                Log.e("MainScreenVM", "Failed to observe work state", throwable)
            }
            .launchIn(viewModelScope)
    }

    private fun emitEvent(event: MainScreenEvent) {
        viewModelScope.launch(Dispatchers.IO) {
            _events.emit(event)
        }
    }

    /**
     * Обновляет конфигурацию для указанного экрана.
     * Используется для синхронизации:
     * - Заголовка TopAppBar
     * - Иконки действия
     * - FAB
     */
    fun updateConfigForScreen(config: ScreenConfig) {
        _configState.value = config
    }
}

sealed class MainScreenEvent {
    data class ShowErrorSnackBar(val messageResId: Int) : MainScreenEvent()
    data class ShowDefaultSnackBar(val messageResId: Int) : MainScreenEvent()
}