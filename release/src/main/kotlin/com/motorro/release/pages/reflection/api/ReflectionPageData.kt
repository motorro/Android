package com.motorro.release.pages.reflection.api

import com.motorro.release.R
import com.motorro.release.api.MainScreenPageData

data object ReflectionPageData : MainScreenPageData {
    override val index: Int = 2
    override val title: Int = R.string.page_reflection
    override val icon: Int = R.drawable.ic_reflection
}
