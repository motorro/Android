package com.motorro.notifications.pages.progress.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.notifications.pages.progress.data.ProgressGesture
import com.motorro.notifications.pages.progress.data.ProgressViewState

@Composable
fun ProgressScreen(state: ProgressViewState, onGesture: (ProgressGesture) -> Unit, modifier: Modifier = Modifier) {
    when (state) {
        ProgressViewState.PromoSettings -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                PromotedSettings( modifier, onGesture)
            } else {
                throw IllegalStateException("Unexpected state $state for API level ${Build.VERSION.SDK_INT}")
            }
        }
        is ProgressViewState.Player -> PlayerScreen(state, modifier, onGesture)
    }
}