package com.motorro.release.pages.pictures.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.release.api.MainScreenUiApi
import com.motorro.release.pages.pictures.data.PicturesUiState
import com.motorro.release.pages.pictures.ui.PicturesScreen
import javax.inject.Inject

/**
 * UI API
 */
class PicturesUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = PicturesPageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        PicturesScreen(
            state = state as PicturesUiState,
            onGesture = onGesture,
            modifier = modifier
        )
    }
}