package com.motorro.release.data

import androidx.compose.runtime.Immutable
import com.motorro.release.api.MainScreenPageData

sealed class MainScreenViewState {
    /**
     * Global loading
     */
    data object Loading : MainScreenViewState()

    /**
     * Main screen view state
     */
    @Immutable
    data class Page(val page: MainScreenPageData, val viewState: Any) : MainScreenViewState()

    /**
     * End of flow
     */
    data object Terminated : MainScreenViewState()
}
