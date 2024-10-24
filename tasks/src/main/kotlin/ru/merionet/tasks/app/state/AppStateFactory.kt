package ru.merionet.tasks.app.state

import ru.merionet.tasks.app.data.AppData
import ru.merionet.tasks.data.Task
import ru.merionet.tasks.data.UserName
import javax.inject.Inject

/**
 * Application state factory
 */
interface AppStateFactory {
    /**
     * Runs application
     */
    fun init(): AppState

    /**
     * Initialization error
     */
    fun initError(error: Throwable): AppState

    /**
     * Switches to login flow
     * @param userName Username if known
     * @param message Login message if any
     */
    fun loggingIn(userName: UserName? = null, message: String? = null): AppState

    /**
     * Logs user out
     */
    fun loggingOut(data: AppData): AppState

    /**
     * Logout error
     */
    fun logoutError(data: AppData, error: Throwable): AppState

    /**
     * Task-list
     */
    fun taskList(data: AppData): AppState

    /**
     * Add/edit task
     */
    fun task(data: AppData, task: Task? = null): AppState

    /**
     * Saving task to task list
     */
    fun savingTask(data: AppData, task: Task): AppState

    /**
     * Application terminated
     */
    fun terminated(): AppState

    /**
     * App state factory implementation
     */
    class Impl @Inject constructor(
        private val createCheckingAuthState: CheckingAuthState.Factory,
        private val createLogin: LoginProxy.Factory,
        private val createLoggingOut: LoggingOutState.Factory,
        private val createTaskList: TaskListState.Factory,
        private val createTask: TaskState.Factory
    ) : AppStateFactory {
        private val context = object : AppContext {
            override val factory: AppStateFactory = this@Impl
        }

        override fun init() = createCheckingAuthState(context)

        override fun initError(error: Throwable) = ErrorState(
            context,
            error,
            onRetry = { init() },
            onBack = { terminated() }
        )

        override fun loggingIn(userName: UserName?, message: String?) = createLogin(
            context = context,
            userName = userName,
            message = message,
            onAuthGranted = {
                // Recheck for simplicity...
                createCheckingAuthState(context)
            }
        )

        override fun loggingOut(data: AppData): AppState = createLoggingOut(
            context = context,
            data = data
        )

        override fun logoutError(data: AppData, error: Throwable): AppState = ErrorState(
            context,
            error,
            onRetry = { loggingOut(data) },
            onBack = { terminated() }
        )

        override fun taskList(data: AppData) = createTaskList(
            context,
            data,
        )

        override fun task(data: AppData, task: Task?) = createTask(
            context,
            data,
            task
        )

        override fun savingTask(data: AppData, task: Task): AppState {
            TODO("Not yet implemented")
        }

        override fun terminated(): AppState = Terminated(context)
    }
}