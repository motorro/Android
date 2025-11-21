package com.motorro.release.pages.reflection.api

import com.motorro.core.log.Logging
import com.motorro.release.api.MainScreenStateApi
import com.motorro.release.data.MainScreenGesture
import com.motorro.release.pages.reflection.data.ReflectionGesture
import com.motorro.release.pages.reflection.data.ReflectionUiState
import com.motorro.release.pages.reflection.state.ReflectionState
import com.motorro.release.pages.reflection.state.ReflectionStateFactory
import javax.inject.Inject

/**
 * Flow API
 */
class ReflectionStateApi @Inject constructor(private val stateFactory: ReflectionStateFactory.Impl) : MainScreenStateApi<ReflectionGesture, ReflectionUiState>, Logging {
    override val data get() = ReflectionPageData

    override fun init(data: Any?): ReflectionState {
        d { "Starting Reflection flow..." }
        return stateFactory.reflection()
    }

    override fun getInitialViewState() = ReflectionUiState("")

    override fun mapGesture(parent: MainScreenGesture): ReflectionGesture? {
        return when {
            parent is MainScreenGesture.PageGesture && data == parent.page -> parent.gesture as ReflectionGesture
            else -> null
        }
    }
}