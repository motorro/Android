package com.motorro.background.pages.review.api

import com.motorro.background.R
import com.motorro.background.api.MainScreenPageData

data object ReviewPageData : MainScreenPageData {
    override val index: Int = 3
    override val title: Int = R.string.page_review
    override val icon: Int = R.drawable.ic_review
}
