package com.motorro.notifications.pages.push.api

import com.motorro.core.log.Logging
import com.motorro.notifications.api.MainScreenStateApi
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.NotificationAction
import com.motorro.notifications.pages.push.data.PushViewState
import com.motorro.notifications.pages.push.state.PushState
import javax.inject.Inject

/**
 * Flow API
 */
class PushStateApi @Inject constructor() : MainScreenStateApi<Unit, PushViewState>, Logging {
    override val data get() = PushPageData

    override fun init(data: NotificationAction?): PushState {
        d { "Starting Reply flow..." }
        return PushState(data)
    }



    override fun getInitialViewState() = PushViewState()

    override fun mapGesture(parent: MainScreenGesture): Unit? = null
}