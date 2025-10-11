package com.motorro.notifications.pages.notification.state

import androidx.annotation.CallSuper
import com.motorro.commonstatemachine.coroutines.CoroutineState
import com.motorro.notifications.pages.notification.data.NotificationData
import com.motorro.notifications.pages.notification.data.NotificationGesture
import com.motorro.notifications.pages.notification.data.NotificationViewState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Notification form
 */
class FormState(private val context: NotificationContext) : CoroutineState<NotificationGesture, NotificationViewState>() {

    private val dataFlow: StateFlow<NotificationData> = context.savedStateHandle.getStateFlow(
        key = DATA_KEY,
        initialValue = NotificationData()
    )

    private fun updateData(block: NotificationData.() -> NotificationData) {
        context.savedStateHandle[DATA_KEY] = dataFlow.value.block()
    }

    private fun isValid(data: NotificationData) = data.title.isNotBlank() && data.text.isNotBlank()

    @CallSuper
    override fun doStart() {
        dataFlow.onEach(::render).launchIn(stateScope)
    }

    override fun doProcess(gesture: NotificationGesture) {
        when (gesture) {
            NotificationGesture.Send -> {
                val data = dataFlow.value
                if (isValid(data)) {
                    setMachineState(context.factory.creating(data))
                }
            }
            is NotificationGesture.TitleChanged -> updateData {
                copy(title = gesture.title)
            }
            is NotificationGesture.TextChanged -> updateData {
                copy(text = gesture.text)
            }
        }
    }

    private fun render(data: NotificationData) {
        setUiState(NotificationViewState.Form(data, isValid(data)))
    }

    companion object {
        private const val DATA_KEY = "notification_data"
    }
}