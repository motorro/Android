package com.motorro.release.api

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Common application view state
 */
interface MainScreenPageData {
    /**
     * Page index in list
     */
    val index: Int

    /**
     * Page title
     */
    @get:StringRes
    val title: Int

    /**
     * Page icon
     */
    @get:DrawableRes
    val icon: Int
}
