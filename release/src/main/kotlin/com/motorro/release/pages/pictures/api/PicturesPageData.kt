package com.motorro.release.pages.pictures.api

import com.motorro.release.R
import com.motorro.release.api.MainScreenPageData

data object PicturesPageData : MainScreenPageData {
    override val index: Int = 1
    override val title: Int = R.string.page_pictures
    override val icon: Int = R.drawable.ic_pictures
}
