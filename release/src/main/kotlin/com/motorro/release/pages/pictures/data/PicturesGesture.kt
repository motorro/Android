package com.motorro.release.pages.pictures.data

sealed class PicturesGesture {
    data object Action : PicturesGesture()
    data class PictureClicked(val selected: Int) : PicturesGesture()
}