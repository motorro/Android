package com.motorro.notifications.api

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.motorro.notifications.data.NotificationAction

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

    /**
     * Path segments
     */
    val pathSegments: List<String> get() = emptyList()

    /**
     * Notification action path if any
     */
    fun matchesAction(action: NotificationAction): Boolean {
        val segments = pathSegments.takeIf { it.isNotEmpty() } ?: return false
        segments.forEachIndexed { index, pathSegment ->
            if (action.pathSegments.getOrNull(index) != pathSegment) {
                return false
            }
        }
        return true
    }
}
