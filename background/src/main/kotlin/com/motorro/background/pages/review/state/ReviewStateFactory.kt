package com.motorro.background.pages.review.state

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.motorro.background.pages.review.data.ReviewData
import javax.inject.Inject
import javax.inject.Provider

interface ReviewStateFactory {

    fun form() : ReviewState

    fun uploading(data: ReviewData) : ReviewState

    fun uploadSuccess(): ReviewState

    fun uploadError(error: Throwable, data: ReviewData): ReviewState

    class Impl @Inject constructor(
        context: Context,
        savedStateHandle: SavedStateHandle,
        private val createUploading: Provider<UploadingState.Factory>
    ) : ReviewStateFactory {

        private val context = object : ReviewContext {
            override val factory: ReviewStateFactory = this@Impl
            override val appContext: Context = context
            override val savedStateHandle: SavedStateHandle = savedStateHandle
        }

        override fun form() = FormState(context)

        override fun uploading(data: ReviewData) = createUploading.get().invoke(context, data)

        override fun uploadSuccess() = UploadSuccessState(context)

        override fun uploadError(error: Throwable, data: ReviewData): ReviewState = UploadErrorState(
            context,
            error,
            data
        )
    }
}

