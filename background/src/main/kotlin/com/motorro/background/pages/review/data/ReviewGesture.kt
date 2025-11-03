package com.motorro.background.pages.review.data

sealed class ReviewGesture {
    data class RatingChanged(val rating: Int) : ReviewGesture()
    data class TextChanged(val text: String) : ReviewGesture()
    data class PhotoAttached(val photo: Photo) : ReviewGesture()
    data class PhotoRemoved(val photo: Photo) : ReviewGesture()
    object Action : ReviewGesture()
}
