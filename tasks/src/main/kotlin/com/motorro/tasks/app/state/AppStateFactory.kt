package com.motorro.tasks.app.state

import com.motorro.tasks.app.data.AppData
import com.motorro.tasks.data.UserName
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
        private val createTaskList: TaskListState.Factory
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
            data
        )

        override fun terminated(): AppState = Terminated(context)
    }
}