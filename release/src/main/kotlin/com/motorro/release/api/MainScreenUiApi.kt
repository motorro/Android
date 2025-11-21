package com.motorro.release.api

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface MainScreenUiApi {
    /**
     * Page data
     */
    val data: MainScreenPageData

    /**
     * Proxied flow UI
     */
    @Composable
    fun Screen(state: Any, onGesture: (Any) -> Unit, modifier: Modifier = Modifier)
}