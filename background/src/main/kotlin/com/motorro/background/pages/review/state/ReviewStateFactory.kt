package com.motorro.background.pages.review.state

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.motorro.background.pages.review.data.ReviewData
import javax.inject.Inject

interface ReviewStateFactory {

    fun form() : ReviewState

    fun uploading(data: ReviewData) : ReviewState

    fun uploadSuccess(): ReviewState

    class Impl @Inject constructor(
        context: Context,
        savedStateHandle: SavedStateHandle
    ) : ReviewStateFactory {

        private val context = object : ReviewContext {
            override val factory: ReviewStateFactory = this@Impl
            override val appContext: Context = context
            override val savedStateHandle: SavedStateHandle = savedStateHandle
        }

        override fun form() = FormState(context)

        override fun uploading(data: ReviewData) = UploadingState(context, data)

        override fun uploadSuccess() = UploadSuccessState(context)
    }
}

