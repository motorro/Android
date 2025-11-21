package com.motorro.release.pages.pictures.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.core.log.Logging
import com.motorro.release.pages.pictures.data.PicturesGesture
import com.motorro.release.pages.pictures.data.PicturesUiState

abstract class PicturesState(private val context: PicturesContext): CommonMachineState<PicturesGesture, PicturesUiState>(), PicturesContext by context, Logging {
    override fun doProcess(gesture: PicturesGesture) {
        w { "Unsupported gesture: $gesture" }
    }
}