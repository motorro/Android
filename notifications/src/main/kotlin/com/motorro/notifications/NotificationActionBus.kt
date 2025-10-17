package com.motorro.notifications

import com.motorro.notifications.data.NotificationAction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import javax.inject.Inject

/**
 * Holds the latest notification action
 */
interface NotificationActionBus {
    /**
     * Latest notification action
     */
    val action: StateFlow<NotificationAction?>

    /**
     * Posts the notification action
     */
    fun post(action: NotificationAction)

    /**
     * Dismisses the notification action
     */
    fun dismiss(): NotificationAction?

    class Impl @Inject constructor(): NotificationActionBus {
        private val _action = MutableStateFlow<NotificationAction?>(null)
        override val action: StateFlow<NotificationAction?> get() = _action.asStateFlow()

        override fun post(action: NotificationAction) {
            _action.value = action
        }

        override fun dismiss() = _action.getAndUpdate { null }
    }
}