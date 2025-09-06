package com.motorro.statemachine.navigation.content

import com.motorro.statemachine.common.data.ui.ContentScreenUiState
import com.motorro.statemachine.common.data.ui.LoadingUiState

sealed interface ContentScreenState {
    /**
     * Represents the content state of the content screen.
     */
    data class Content(val state: ContentScreenUiState) : ContentScreenState

    /**
     * Represents the loading state of the content screen.
     */
    data class Loading(val state: LoadingUiState) : ContentScreenState
}