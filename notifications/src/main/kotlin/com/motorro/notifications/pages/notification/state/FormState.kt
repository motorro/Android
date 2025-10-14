package com.motorro.notifications.pages.notification.state

import androidx.annotation.CallSuper
import com.motorro.notifications.MyNotificationChannel
import com.motorro.notifications.pages.notification.data.NotificationData
import com.motorro.notifications.pages.notification.data.NotificationGesture
import com.motorro.notifications.pages.notification.data.NotificationViewState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

/**
 * Notification form
 */
class FormState(context: NotificationContext) : BaseNotificationState(context) {

    private val dataFlow: StateFlow<NotificationData> = context.savedStateHandle.getStateFlow(
        key = DATA_KEY,
        initialValue = NotificationData()
    )

    private fun updateData(block: NotificationData.() -> NotificationData) {
        savedStateHandle[DATA_KEY] = dataFlow.value.block()
    }

    private fun isValid(data: NotificationData) = data.title.isNotBlank() && data.text.isNotBlank()

    @CallSuper
    override fun doStart() {
        super.doStart()
        dataFlow.onEach(::render).launchIn(stateScope)
    }

    override fun doProcess(gesture: NotificationGesture) {
        when (gesture) {
            NotificationGesture.Send -> {
                val toSend = dataFlow.value
                if (isValid(toSend)) {
                    setMachineState(factory.sending(toSend))
                }
            }
            NotificationGesture.Dismiss -> setMachineState(factory.dismissingLatest())
            is NotificationGesture.TitleChanged -> updateData {
                copy(title = gesture.title)
            }
            is NotificationGesture.TextChanged -> updateData {
                copy(text = gesture.text)
            }
            is NotificationGesture.ChannelChanged -> updateData {
                copy(channel = gesture.channel)
            }
        }
    }

    private fun render(data: NotificationData) {
        setUiState(
            NotificationViewState.Form(
                data = data,
                availableChannels = MyNotificationChannel.entries,
                sendEnabled = isValid(data)
            )
        )
    }

    private companion object {
        const val DATA_KEY = "notification_data"
    }
}