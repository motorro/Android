package com.motorro.activitynav.data

import com.motorro.activitynav.ActivityId

/**
 * Activity Status
 */
data class ActivityState(
    val activityId: ActivityId,
    val state: ActivityStatus = ActivityStatus.UNKNOWN,
    val intentCount: Int = 0
)

enum class ActivityStatus {
    UNKNOWN,
    CREATED,
    STARTED,
    RESUMED,
    PAUSED,
    STOPPED
}
