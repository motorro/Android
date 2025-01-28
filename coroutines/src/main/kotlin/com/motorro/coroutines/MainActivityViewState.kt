package com.motorro.coroutines

import com.motorro.coroutines.network.data.Profile

/**
 * Состояние основного экрана
 */
sealed class MainActivityViewState {
    /**
     * No user - show login
     */
    data object Login: MainActivityViewState()

    /**
     * Loading
     */
    data object Loading: MainActivityViewState()

    /**
     * Have active user - show content
     */
    data class Content(val profile: Profile): MainActivityViewState()
}