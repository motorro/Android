package com.motorro.activitynav

import android.app.Activity

class ActivityId private constructor(val id: String, val taskId: Int) {
    companion object {
        private var activityCounter = 1

        fun create(activity: Activity) = ActivityId(
            "${activity::class.simpleName} - ${activityCounter++}",
            activity.taskId
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ActivityId

        if (id != other.id) return false
        if (taskId != other.taskId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + taskId
        return result
    }

    override fun toString(): String {
        return "ActivityId(id='$id', taskId=$taskId)"
    }
}