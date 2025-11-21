package com.motorro.release.pages.pictures.state

import com.motorro.release.pages.pictures.data.PicturesGesture
import com.motorro.release.pages.pictures.data.PicturesUiState
import com.motorro.release.pages.pictures.data.pictures

class PicturePreview(context: PicturesContext, private val selected: Int) : PicturesState(context) {
    override fun doStart() {
        setUiState(PicturesUiState.Preview(pictures, selected))
    }

    override fun doProcess(gesture: PicturesGesture) {
        when (gesture) {
            is PicturesGesture.Action -> {
                setMachineState(factory.pictures())
            }
            else -> super.doProcess(gesture)
        }
    }
}