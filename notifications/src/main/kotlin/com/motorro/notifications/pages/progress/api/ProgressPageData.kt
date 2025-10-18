package com.motorro.notifications.pages.progress.api

import com.motorro.notifications.R
import com.motorro.notifications.api.MainScreenPageData

data object ProgressPageData : MainScreenPageData {

    override val pathSegments: List<String> = listOf("progress")

    override val index: Int = 3
    override val title: Int = R.string.page_progress
    override val icon: Int = R.drawable.ic_progress
}
