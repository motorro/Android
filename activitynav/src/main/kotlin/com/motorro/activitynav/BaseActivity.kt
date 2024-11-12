package com.motorro.activitynav

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.motorro.activitynav.data.Task
import com.motorro.activitynav.data.print
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseActivity : AppCompatActivity() {
    lateinit var activityId: ActivityId
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        activityId = ActivityId.create(this)
        super.onCreate(savedInstanceState)

        subscribeToTasks()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        (application as App).countIntent(this)
    }

    private fun subscribeToTasks() = (application as App).tasks
        .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
        .onEach { onTasksChanged(it) }
        .launchIn(lifecycleScope)

    protected open fun onTasksChanged(tasks: List<Task>) {
        Log.i(TASKS_TAG, "Tasks changed:")
        tasks.forEach { Log.i(TASKS_TAG, it.print()) }
    }

    companion object {
        const val TASKS_TAG = "TASKS"
    }
}