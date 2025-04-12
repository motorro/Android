package com.motorro.network.session

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

interface SessionManager {
    val loggedIn: StateFlow<Boolean>
    val token: StateFlow<String?>

    @OptIn(DelicateCoroutinesApi::class)
    class Impl : SessionManager {
        override val loggedIn: StateFlow<Boolean> = flowOf(true).stateIn(
            scope = GlobalScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )

        override val token: StateFlow<String?> = flowOf(null).stateIn(
            scope = GlobalScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )
    }
}