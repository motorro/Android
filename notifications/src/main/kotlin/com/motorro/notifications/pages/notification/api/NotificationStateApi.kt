package com.motorro.notifications.pages.notification.api

import com.motorro.core.log.Logging
import com.motorro.notifications.api.MainScreenStateApi
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.pages.notification.data.NotificationGesture
import com.motorro.notifications.pages.notification.data.NotificationViewState
import com.motorro.notifications.pages.notification.state.NotificationState
import com.motorro.notifications.pages.notification.state.NotificationStateFactory
import javax.inject.Inject

/**
 * Flow API
 */
class NotificationStateApi @Inject constructor(private val stateFactory: NotificationStateFactory.Impl) : MainScreenStateApi<NotificationGesture, NotificationViewState>, Logging {
    override val data get() = NotificationPageData

    override fun init(data: Any?): NotificationState {
        d { "Starting Basic Notification flow..." }
        return stateFactory.form()
    }

    override fun getInitialViewState() = NotificationViewState.Loading

    override fun mapGesture(parent: MainScreenGesture): NotificationGesture? {
        return when {
            parent is MainScreenGesture.PageGesture && data == parent.page -> parent.gesture as NotificationGesture
            else -> null
        }
    }
}