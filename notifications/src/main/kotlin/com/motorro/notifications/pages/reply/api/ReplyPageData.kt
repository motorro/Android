package com.motorro.notifications.pages.reply.api

import com.motorro.notifications.R
import com.motorro.notifications.api.MainScreenPageData

data object ReplyPageData : MainScreenPageData {

    override val pathSegments: List<String> = listOf("reply")

    const val REPLY_TEXT_PARAM = "text"

    override val index: Int = 2
    override val title: Int = R.string.page_reply
    override val icon: Int = R.drawable.ic_reply
}
