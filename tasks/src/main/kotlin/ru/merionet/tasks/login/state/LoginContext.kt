package ru.merionet.tasks.login.state

/**
 * Common data for all login states
 */
interface LoginContext {
    /**
     * State factory
     */
    val factory: LoginStateFactory
}