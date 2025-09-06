package com.motorro.statemachine.navigation.login

sealed interface LoginNavigationEvent {
    /**
     * Represents a navigation request to the content screen.
     */
    data object NavigateToContent : LoginNavigationEvent
}