package com.motorro.tasks.app.state

import com.motorro.tasks.app.data.AppData
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
import javax.inject.Inject

/**
 * Task list
 */
class TaskListState(context: AppContext, private val data: AppData) : BaseAppState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        setUiState(AppUiState.TaskList)
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            AppGesture.Back -> {
                d { "Back. Terminating..." }
                setMachineState(factory.terminated())
            }
            AppGesture.Logout -> {
                d { "Logout. Returning to login screen..." }
                setMachineState(factory.loggingOut(data))
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor() {
        operator fun invoke(context: AppContext, data: AppData) = TaskListState(
            context,
            data
        )
    }
}