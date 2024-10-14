package ru.merionet.tasks.app.state

/**
 * Common inter-state data for application
 */
interface AppContext {
    /**
     * State factory
     */
    val factory: AppStateFactory
}