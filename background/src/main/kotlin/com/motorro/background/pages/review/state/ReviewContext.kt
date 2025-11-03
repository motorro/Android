package com.motorro.background.pages.review.state

import android.content.Context
import androidx.lifecycle.SavedStateHandle

interface ReviewContext {
    val factory: ReviewStateFactory
    val appContext: Context
    val savedStateHandle: SavedStateHandle
}