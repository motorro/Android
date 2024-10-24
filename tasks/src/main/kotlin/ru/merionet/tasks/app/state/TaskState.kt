package ru.merionet.tasks.app.state

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import ru.merionet.tasks.app.data.AppData
import ru.merionet.tasks.app.data.AppGesture
import ru.merionet.tasks.app.data.AppUiState
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.newTaskTemplate
import javax.inject.Inject

/**
 * New task form
 */
class TaskState(
    context: AppContext,
    private val data: AppData,
    private var passedTask: Task?,
    private val clock: Clock,
    private val timeZone: TimeZone
) : BaseAppState(context) {

    /**
     * Local task changes
     */
    private var task: Task? = passedTask

    /**
     * Updates task data
     */
    private inline fun updateTask(block: Task.() -> Task): Task {
        val updated = (task ?: newTaskTemplate(data.user.name)).block()
        if (updated != task) {
            task = updated
            render()
        }
        return updated
    }

    /**
     * A part of [start] template to initialize state
     */
    override fun doStart() {
        super.doStart()
        render()
    }

    /**
     * Checks if task is valid
     */
    private fun Task?.isValid(): Boolean = true == this?.title?.isNotBlank()

    /**
     * Renders task screen
     */
    private fun render() {
        setUiState(AppUiState.EditTask(
            title = task?.title.orEmpty(),
            description = task?.description.orEmpty(),
            due = task?.due,
            completeVisible = true == passedTask?.complete?.not(),
            saveEnabled = task != passedTask && task.isValid()
        ))
    }

    /**
     * Updates due date
     */
    private fun updateDate(value: LocalDate) {
        val currentDateTime = clock.now().toLocalDateTime(timeZone)
        if (currentDateTime.date > value) {
            w { "Invalid date" }
            return
        }
        updateTask {
            copy(due = LocalDateTime(value, due?.time ?: LocalTime(23, 59)))
        }
    }

    /**
     * Updates due time
     */
    private fun updateTime(value: LocalTime) {
        val currentDateTime = clock.now().toLocalDateTime(timeZone)
        if (currentDateTime.time > value) {
            w { "Invalid time" }
            return
        }
        updateTask {
            copy(due = LocalDateTime(due?.date ?: currentDateTime.date, value))
        }
    }

    /**
     * Checks task and saves it if valid
     */
    private fun onSave(task: Task?) {
        when (task) {
            null -> {
                d { "No new task!" }
                return
            }
            passedTask -> {
                d { "No changes. Returning to task list..." }
                setMachineState(factory.taskList(data))
            }
            else -> {
                if (task.isValid().not()) {
                    d { "Invalid task data" }
                    return
                }
                d { "Task updated. Saving..." }
                setMachineState(factory.savingTask(data, task))
            }
        }
    }

    /**
     * A part of [process] template to process UI gesture
     */
    override fun doProcess(gesture: AppGesture) {
        when(gesture) {
            is AppGesture.EditTask.TitleChanged -> {
                updateTask {
                    copy(title = gesture.value)
                }
            }
            is AppGesture.EditTask.DescriptionChanged -> {
                updateTask {
                    copy(description = gesture.value)
                }
            }
            is AppGesture.EditTask.DateSelected -> updateDate(gesture.value)
            is AppGesture.EditTask.TimeSelected -> updateTime(gesture.value)
            AppGesture.Back -> {
                d { "Back pressed" }
                setMachineState(factory.taskList(data))
            }
            AppGesture.EditTask.CompleteClicked -> {
                d { "Task complete clicked" }
                onSave(task?.copy(complete = true))
            }
            AppGesture.EditTask.SaveClicked -> {
                d { "Save clicked" }
                onSave(task)
            }
            else -> super.doProcess(gesture)
        }
    }

    /**
     * State factory to inject all the external dependencies and keep the main factory
     * clean
     */
    class Factory @Inject constructor() {
        operator fun invoke(context: AppContext, data: AppData, task: Task?) = TaskState(
            context,
            data,
            task,
            Clock.System,
            TimeZone.currentSystemDefault()
        )
    }
}