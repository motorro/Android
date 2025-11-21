package com.motorro.release.pages.pictures.api

import com.motorro.core.log.Logging
import com.motorro.release.api.MainScreenStateApi
import com.motorro.release.data.MainScreenGesture
import com.motorro.release.pages.pictures.data.PicturesGesture
import com.motorro.release.pages.pictures.data.PicturesUiState
import com.motorro.release.pages.pictures.state.PicturesState
import com.motorro.release.pages.pictures.state.PicturesStateFactory
import javax.inject.Inject

/**
 * Flow API
 */
class PicturesStateApi @Inject constructor(private val stateFactory: PicturesStateFactory.Impl) : MainScreenStateApi<PicturesGesture, PicturesUiState>, Logging {
    override val data get() = PicturesPageData

    override fun init(data: Any?): PicturesState {
        d { "Starting Pictures flow..." }
        return stateFactory.pictures()
    }

    override fun getInitialViewState() = PicturesUiState.Empty

    override fun mapGesture(parent: MainScreenGesture): PicturesGesture? {
        return when {
            parent is MainScreenGesture.PageGesture && data == parent.page -> parent.gesture as PicturesGesture
            else -> null
        }
    }
}