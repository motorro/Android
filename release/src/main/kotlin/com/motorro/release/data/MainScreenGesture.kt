package com.motorro.release.data

import com.motorro.release.api.MainScreenPageData

sealed class MainScreenGesture {
    data object Back : MainScreenGesture()

    data class Navigate(val page: MainScreenPageData) : MainScreenGesture()
    data class PageGesture(val page: MainScreenPageData, val gesture: Any) : MainScreenGesture()
}