package com.motorro.network.session

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

interface SessionManager {
    val loggedIn: StateFlow<Boolean>
    val token: StateFlow<String?>
    fun saveToken(token: String?)

    @OptIn(DelicateCoroutinesApi::class)
    class Impl : SessionManager {

        private val _token: MutableStateFlow<String?> = MutableStateFlow(null)
        override val token: StateFlow<String?> get() = _token.asStateFlow()
        override fun saveToken(token: String?) {
            _token.value = token
        }

        override val loggedIn: StateFlow<Boolean> = _token.map { null != it }.stateIn(
            scope = GlobalScope,
            started = SharingStarted.Lazily,
            initialValue = false
        )
    }
}