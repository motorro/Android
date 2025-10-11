package com.motorro.notifications.pages.notification.api

import com.motorro.notifications.R
import com.motorro.notifications.api.MainScreenPageData

data object NotificationPageData : MainScreenPageData {
    override val index: Int = 1
    override val title: Int = R.string.page_notification
    override val icon: Int = R.drawable.ic_notification
}
