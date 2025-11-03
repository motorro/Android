package com.motorro.background.pages.review.data

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.parcelize.Parcelize

/**
 * Review data
 */
@Parcelize
data class ReviewData(
    val rating: Int = 0,
    val text: String = "",
    val photos: List<Photo> = emptyList()
) : Parcelable

/**
 * Review attachment photo
 */
@JvmInline
@Parcelize
value class Photo(val uri: Uri) : Parcelable

private const val KEY_REVIEW = "review"

/**
 * Saved review data
 */
val SavedStateHandle.reviewData: StateFlow<ReviewData> get() = getStateFlow(KEY_REVIEW, ReviewData())

/**
 * Update review data
 */
fun SavedStateHandle.updateReview(block: ReviewData.() -> ReviewData) {
    set(KEY_REVIEW, reviewData.value.block())
}

/**
 * Delete review data
 */
fun SavedStateHandle.deleteReview() {
    remove<ReviewData>(KEY_REVIEW)
}