package com.motorro.notifications.pages.progress.api

import com.motorro.core.log.Logging
import com.motorro.notifications.api.MainScreenStateApi
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.NotificationAction
import com.motorro.notifications.pages.progress.data.ProgressGesture
import com.motorro.notifications.pages.progress.data.ProgressViewState
import com.motorro.notifications.pages.progress.state.ProgressState
import com.motorro.notifications.pages.progress.state.ProgressStateFactory
import javax.inject.Inject

/**
 * Flow API
 */
class ProgressStateApi @Inject constructor(private val stateFactory: ProgressStateFactory.Impl) : MainScreenStateApi<ProgressGesture, ProgressViewState>, Logging {
    override val data get() = ProgressPageData

    override fun init(data: NotificationAction?): ProgressState {
        d { "Starting Progress flow..." }
        return stateFactory.checkPromo()
    }

    override fun getInitialViewState() = ProgressViewState.Player.Idle

    override fun mapGesture(parent: MainScreenGesture): ProgressGesture? {
        return when {
            parent is MainScreenGesture.PageGesture && data == parent.page -> parent.gesture as ProgressGesture
            else -> null
        }
    }
}