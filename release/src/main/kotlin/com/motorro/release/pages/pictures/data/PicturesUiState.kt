package com.motorro.release.pages.pictures.data

sealed class PicturesUiState {
    data class PictureList(val pictures: List<Picture>) : PicturesUiState()
    data class Preview(val pictures: List<Picture>, val selected: Int) : PicturesUiState()

    companion object {
        val Empty = PictureList(emptyList())
    }
}
