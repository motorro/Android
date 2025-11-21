package com.motorro.release.pages.pictures.state

import com.motorro.release.pages.pictures.data.PicturesGesture
import com.motorro.release.pages.pictures.data.PicturesUiState
import com.motorro.release.pages.pictures.data.pictures

class PictureList(context: PicturesContext) : PicturesState(context) {
    override fun doStart() {
        setUiState(PicturesUiState.PictureList(pictures))
    }

    override fun doProcess(gesture: PicturesGesture) {
        when (gesture) {
            is PicturesGesture.PictureClicked -> {
                setMachineState(factory.picturePreview(gesture.selected))
            }
            else -> super.doProcess(gesture)
        }
    }
}