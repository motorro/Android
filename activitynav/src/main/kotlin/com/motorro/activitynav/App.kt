package com.motorro.activitynav

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.motorro.activitynav.data.ActivityState
import com.motorro.activitynav.data.ActivityStatus
import com.motorro.activitynav.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class App : Application() {
    private lateinit var tracker: ActivityTracker

    lateinit var tasks: StateFlow<List<Task>>
        private set

    fun countIntent(activity: Activity) {
        tracker.countIntent(activity)
    }

    override fun onCreate() {
        super.onCreate()
        tracker = ActivityTracker()
        registerActivityLifecycleCallbacks(tracker)
        tasks = tracker.tasks
    }
}

/**
 * Tracks all activities in application
 */
class ActivityTracker : Application.ActivityLifecycleCallbacks {
    /**
     * State flow for tasks
     */
    val tasks = MutableStateFlow<List<Task>>(emptyList())

    private val activities = LinkedHashMap<ActivityId, ActivityState>()

    private inline fun updateActivityState(activity: Activity, block: (BaseActivity, ActivityState) -> ActivityState?) {
        activity as BaseActivity

        val soFar = activities[activity.activityId] ?: ActivityState(activity.activityId)

        val update = block(activity, soFar)
        if (null != update) {
            activities[update.activityId] = update
        } else {
            activities.remove(activity.activityId)
        }

        tasks.update {
            activities.values
                .groupBy { it.activityId.taskId }
                .map { (taskId, activities) ->
                    Task(taskId, activities)
                }
        }
    }

    private fun updateActivityStatus(activity: Activity, state: ActivityStatus) {
        updateActivityState(activity) { _, s ->
            s.copy(state = state)
        }
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        updateActivityState(activity) { _, s ->
            s.copy(state = ActivityStatus.CREATED, intentCount = 1)
        }
    }

    override fun onActivityStarted(activity: Activity) {
        updateActivityStatus(activity, ActivityStatus.STARTED)
    }

    override fun onActivityResumed(activity: Activity) {
        updateActivityStatus(activity, ActivityStatus.RESUMED)
    }

    override fun onActivityPaused(activity: Activity) {
        updateActivityStatus(activity, ActivityStatus.PAUSED)
    }

    override fun onActivityStopped(activity: Activity) {
        updateActivityStatus(activity, ActivityStatus.STOPPED)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // Nothing to do
    }

    override fun onActivityDestroyed(activity: Activity) {
        updateActivityState(activity) { _, _ -> null }
    }

    fun countIntent(activity: Activity) {
        updateActivityState(activity) { _, s ->
            s.copy(intentCount = s.intentCount + 1)
        }
    }
}