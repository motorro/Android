package com.motorro.background.pages.blog.api

import com.motorro.background.R
import com.motorro.background.api.MainScreenPageData

data object BlogPageData : MainScreenPageData {
    override val index: Int = 2
    override val title: Int = R.string.page_blog
    override val icon: Int = R.drawable.ic_blog
}
