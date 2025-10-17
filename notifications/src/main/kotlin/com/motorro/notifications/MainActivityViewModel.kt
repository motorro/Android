package com.motorro.notifications

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.motorro.commonstatemachine.coroutines.FlowStateMachine
import com.motorro.core.log.Logging
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.MainScreenViewState
import com.motorro.notifications.state.MainScreenStateFactory
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn


@HiltViewModel(assistedFactory = MainActivityViewModel.Factory::class)
class MainActivityViewModel @AssistedInject constructor(
    private val factory: MainScreenStateFactory,
    private val notificationActionBus: NotificationActionBus,
    @Assisted intent: Intent
) : ViewModel(), Logging {

    val stateMachine = FlowStateMachine(MainScreenViewState.Loading) {
        factory.init(intent)
    }

    val uiState: StateFlow<MainScreenViewState> = stateMachine
        .uiState
        .combine(
            flow = notificationActionBus.action,
            transform = { uiState, action ->
                (uiState as? MainScreenViewState.Page)?.copy(notificationAction = action) ?: uiState
            }
        )
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = MainScreenViewState.Loading
        )

    fun process(gesture: MainScreenGesture) = stateMachine.process(gesture)

    fun processAction() {
        val action = notificationActionBus.dismiss()
        if (null != action) {
            i { "Notification action processing: $action" }
            stateMachine.setMachineState(factory.handlingAction(action))
        }
    }

    fun dismissAction() {
        notificationActionBus.dismiss()
    }

    fun processIntent(intent: Intent) {
        i { "New intent: $intent" }
        val action = intent.asNotificationActionOrNull()
        if (null != action) {
            i { "Notification intent: $intent" }
            notificationActionBus.post(action)
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(intent: Intent): MainActivityViewModel
    }
}

