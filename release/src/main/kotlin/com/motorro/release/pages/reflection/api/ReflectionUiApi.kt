package com.motorro.release.pages.reflection.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.release.api.MainScreenUiApi
import com.motorro.release.pages.reflection.data.ReflectionUiState
import com.motorro.release.pages.reflection.ui.ReflectionScreen
import javax.inject.Inject

/**
 * UI API
 */
class ReflectionUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = ReflectionPageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        ReflectionScreen(
            state = state as ReflectionUiState,
            modifier = modifier
        )
    }
}