package ru.merionet.tasks.app.state

import kotlinx.coroutines.launch
import ru.merionet.tasks.app.data.AppData
import ru.merionet.tasks.app.data.AppError
import ru.merionet.tasks.app.data.AppUiState
import ru.merionet.tasks.app.data.toApp
import ru.merionet.tasks.app.repository.TasksRepository
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskId
import javax.inject.Inject

/**
 * Saves task to repository
 */
abstract class UpdatingTaskState(
    context: AppContext,
    protected val data: AppData,
    private val tasksRepository: TasksRepository
) : BaseAppState(context) {
    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        setUiState(AppUiState.Loading())
        update()
    }

    /**
     * Updates task data
     */
    abstract suspend fun TasksRepository.runUpdate()

    /**
     * Error handler
     */
    abstract fun onError(error: AppError)

    /**
     * Updates the repository
     */
    private fun update() {
        d { "Updating task..." }
        stateScope.launch {
            try {
                tasksRepository.runUpdate()
                setMachineState(factory.taskList(data))
            } catch (e: Throwable) {
                w(e) { "Error updating task" }
                onError(e.toApp())
            }
        }
    }

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor(private val tasksRepository: TasksRepository) {
        fun upsert(context: AppContext, data: AppData, task: Task): AppState = UpsertTask(
            context,
            data,
            task,
            tasksRepository
        )

        fun delete(context: AppContext, data: AppData, taskId: TaskId): AppState = DeleteTask(
            context,
            data,
            taskId,
            tasksRepository
        )
    }
}

/**
 * Upsert task
 */
class UpsertTask(context: AppContext, data: AppData, private val task: Task, tasksRepository: TasksRepository) : UpdatingTaskState(context, data, tasksRepository) {
    /**
     * Updates task data
     */
    override suspend fun TasksRepository.runUpdate() {
        upsertTaskAsync(data.user.name, task)
    }

    /**
     * Error handler
     */
    override fun onError(error: AppError) {
        setMachineState(factory.savingTaskError(data, task, error))
    }
}

/**
 * Delete task
 */
class DeleteTask(context: AppContext, data: AppData, private val taskId: TaskId, tasksRepository: TasksRepository) : UpdatingTaskState(context, data, tasksRepository) {
    /**
     * Updates task data
     */
    override suspend fun TasksRepository.runUpdate() {
        deleteTaskAsync(data.user.name, taskId)
    }

    /**
     * Error handler
     */
    override fun onError(error: AppError) {
        setMachineState(factory.deletingTaskError(data, taskId, error))
    }
}