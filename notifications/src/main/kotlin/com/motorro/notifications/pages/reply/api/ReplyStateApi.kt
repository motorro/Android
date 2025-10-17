package com.motorro.notifications.pages.reply.api

import com.motorro.core.log.Logging
import com.motorro.notifications.api.MainScreenStateApi
import com.motorro.notifications.data.MainScreenGesture
import com.motorro.notifications.data.NotificationAction
import com.motorro.notifications.pages.reply.data.ReplyViewState
import com.motorro.notifications.pages.reply.state.ReplyState
import javax.inject.Inject

/**
 * Flow API
 */
class ReplyStateApi @Inject constructor() : MainScreenStateApi<Unit, ReplyViewState>, Logging {
    override val data get() = ReplyPageData

    override fun init(data: NotificationAction?): ReplyState {
        d { "Starting Reply flow..." }
        return ReplyState(data)
    }

    override fun getInitialViewState() = ReplyViewState()

    override fun mapGesture(parent: MainScreenGesture): Unit? = null
}