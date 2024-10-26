package ru.merionet.tasks.app.state

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import ru.merionet.core.coroutines.DispatcherProvider
import ru.merionet.core.lce.LceState
import ru.merionet.tasks.app.data.AppData
import ru.merionet.tasks.app.data.AppError
import ru.merionet.tasks.app.data.AppGesture
import ru.merionet.tasks.app.data.AppUiState
import ru.merionet.tasks.app.repository.TasksRepository
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.TaskId
import javax.inject.Inject

/**
 * Task list
 */
class TaskListState(
    context: AppContext,
    private val data: AppData,
    private val tasksRepository: TasksRepository,
    private val dispatchers: DispatcherProvider
) : BaseAppState(context) {

    private var tasks: Collection<Task> = emptyList()
    private var updateStatus: LceState<Unit, AppError> = LceState.Content(Unit)

    private val tasksChannel = Channel<Collection<Task>>(capacity = Channel.CONFLATED)
    private val toggles = MutableSharedFlow<TaskId>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

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
        subscribeToggles()
        update()
    }

    /**
     * Subscribes task list
     */
    private fun subscribeTasks() {
        tasksRepository.getTasks(data.user.name)
            .onEach { tasks = it }
            .onEach { tasksChannel.send(it) }
            .onEach { render() }
            .flowOn(Dispatchers.Default)
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
     * Subscribes toggles and schedules task updates on each click
     * Consider some flow debounce or update batching in real world app
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    private fun subscribeToggles() {
        tasksChannel.consumeAsFlow()
            .map { it.associateBy { task -> task.id } }
            .flatMapLatest { taskMap ->
                toggles
                    .mapNotNull { taskMap[it] }
                    .map { it.copy(complete = it.complete.not()) }
                    .distinctUntilChanged { old, new -> old.complete != new.complete }
                    .onEach { update ->
                        tasksRepository.upsertTaskAsync(
                            data.user.name,
                            update
                        )
                    }
            }
            .flowOn(dispatchers.default)
            .launchIn(stateScope)
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
            is AppGesture.TaskList.TaskToggled -> {
                toggles.tryEmit(gesture.id)
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
    class Factory @Inject constructor(
        private val tasksRepository: TasksRepository,
        private val dispatchers: DispatcherProvider
    ) {
        operator fun invoke(context: AppContext, data: AppData) = TaskListState(
            context,
            data,
            tasksRepository,
            dispatchers
        )
    }
}