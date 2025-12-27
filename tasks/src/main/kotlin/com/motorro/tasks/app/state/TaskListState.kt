package com.motorro.tasks.app.state

import com.motorro.core.lce.LceState
import com.motorro.tasks.app.data.AppData
import com.motorro.tasks.app.data.AppError
import com.motorro.tasks.app.data.AppGesture
import com.motorro.tasks.app.data.AppUiState
import com.motorro.tasks.app.repository.TasksRepository
import com.motorro.tasks.data.Task
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * Task list
 */
class TaskListState(
    context: AppContext,
    private val data: AppData,
    private val tasksRepository: TasksRepository
) : BaseAppState(context) {

    private var tasks: Collection<Task> = emptyList()
    private var updateStatus: LceState<Unit, AppError> = LceState.Content(Unit)

    /**
     * Combined loading status
     */
    private fun isLoading(): Boolean {
        return updateStatus is LceState.Loading
    }

    /**
     * Don't allow concurrent updates
     */
    private inline fun ifNotLoading(block: () -> Unit) {
        if (isLoading()) {
            return
        }
        block()
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        subscribeTasks()
        update()
    }

    /**
     * Subscribes task list
     */
    private fun subscribeTasks() {
        tasksRepository.getTasks(data.user.name)
            .onEach { tasks = it }
            .onEach { render() }
            .launchIn(stateScope)
    }

    /**
     * Updates task list from server
     */
    private fun update() = ifNotLoading {
        d { "Updating..." }
        tasksRepository.update(data.user.name)
            .onEach { updateStatus = it }
            .onEach { render() }
            .launchIn(stateScope)
    }

    private fun render() {
        setUiState(
            AppUiState.TaskList(
                tasks,
                isLoading(),
                (updateStatus as? LceState.Error)?.error
            )
        )
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            AppGesture.TaskList.AddClicked -> {
                d { "Adding task..." }
                setMachineState(factory.task(data))
            }
            is AppGesture.TaskList.TaskClicked -> {
                val task = tasks.find { gesture.id == it.id } ?: return
                d { "Task clicked: ${gesture.id}" }
                setMachineState(factory.task(data, task))
            }
            AppGesture.Refresh -> {
                d { "Refresh gesture" }
                update()
            }
            AppGesture.Back -> {
                d { "Back gesture" }
                terminate()
            }
            AppGesture.DismissError -> {
                d { "Dismiss gesture" }
                dismissError()
            }
            AppGesture.Logout -> {
                d { "Logout gesture" }
                logout()
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * Dismisses error if any
     */
    private fun dismissError() {
        val error = (updateStatus as? LceState.Error)?.error ?: return
        d { "Dismissing error: $error" }
        when {
            error.retriable -> {
                d { "Retrying..." }
                update()
            }
            error is AppError.Authentication -> logout()
            else -> terminate()
        }
    }

    /**
     * Logs-out
     */
    private fun logout() {
        d { "Logging out..." }
        setMachineState(factory.loggingOut(this@TaskListState.data))
    }

    /**
     * Terminates
     */
    private fun terminate() {
        d { "Terminating..." }
        setMachineState(factory.terminated())
    }

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor(private val tasksRepository: TasksRepository) {
        operator fun invoke(context: AppContext, data: AppData) = TaskListState(
            context,
            data,
            tasksRepository
        )
    }
}