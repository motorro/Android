package com.motorro.cookbook.recipelist.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

/**
 * Common glide
 */
val LocalGlideRequestManager: ProvidableCompositionLocal<RequestManager> = staticCompositionLocalOf {
    throw RuntimeException("Glide is not provided")
}

/**
 * Provides local glide dependencies
 */
@Composable
fun WithLocalGlide(block: @Composable () -> Unit) {
    CompositionLocalProvider(LocalGlideRequestManager provides Glide.with(LocalContext.current)) {
        block()
    }
}