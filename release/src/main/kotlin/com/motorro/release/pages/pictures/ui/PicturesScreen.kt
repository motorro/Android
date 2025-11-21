package com.motorro.release.pages.pictures.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.motorro.release.pages.pictures.data.PicturesGesture
import com.motorro.release.pages.pictures.data.PicturesUiState


@Composable
fun PicturesScreen(
    state: PicturesUiState,
    onGesture: (PicturesGesture) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is PicturesUiState.PictureList -> Gallery(
            pictures = state.pictures,
            onGesture = onGesture,
            modifier = modifier
        )
        is PicturesUiState.Preview -> Preview(
            state = state,
            onGesture = onGesture,
            modifier = modifier
        )
    }
}

