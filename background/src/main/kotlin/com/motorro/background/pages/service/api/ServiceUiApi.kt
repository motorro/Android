package com.motorro.background.pages.service.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.background.api.MainScreenUiApi
import com.motorro.background.pages.service.data.ServiceUiState
import com.motorro.background.pages.service.ui.ServiceScreen
import javax.inject.Inject

/**
 * UI API
 */
class ServiceUiApi @Inject constructor(): MainScreenUiApi {
    override val data get() = ServicePageData

    @Composable
    override fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier) {
        ServiceScreen(
            state = state as ServiceUiState,
            onGesture = onGesture,
            modifier = modifier
        )
    }
}