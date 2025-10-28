package com.motorro.background.pages.service.api

import com.motorro.background.R
import com.motorro.background.api.MainScreenPageData

data object ServicePageData : MainScreenPageData {
    override val index: Int = 1
    override val title: Int = R.string.page_service
    override val icon: Int = R.drawable.ic_service
}
