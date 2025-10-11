package com.motorro.notifications.api

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.collections.immutable.ImmutableList

/**
 * Main screen flows
 */
val LocalPages = staticCompositionLocalOf<ImmutableList<MainScreenUiApi>> {
    error("No pages provided")
}

/**
 * Provides local pages
 */
@Composable
fun WithLocalPages(pages: ImmutableList<MainScreenUiApi>, block: @Composable () -> Unit) {
    CompositionLocalProvider(LocalPages provides pages) {
        block()
    }
}