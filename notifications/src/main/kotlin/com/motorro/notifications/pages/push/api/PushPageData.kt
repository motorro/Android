package com.motorro.notifications.pages.push.api

import com.motorro.notifications.R
import com.motorro.notifications.api.MainScreenPageData
import com.motorro.notifications.data.NotificationAction

data object PushPageData : MainScreenPageData {

    override val pathSegments: List<String> = listOf("push")

    const val PUSH_TITLE_PARAM = "title"
    const val PUSH_MESSAGE_PARAM = "message"
    const val PUSH_DATA_PARAM = "data"

    override val index: Int = 3
    override val title: Int = R.string.page_push
    override val icon: Int = R.drawable.ic_push

    override fun matchesAction(action: NotificationAction): Boolean {
        return super.matchesAction(action).takeIf { it } ?: action.intent.hasExtra(PUSH_DATA_PARAM)
    }
}
