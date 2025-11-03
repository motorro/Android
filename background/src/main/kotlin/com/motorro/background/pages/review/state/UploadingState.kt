package com.motorro.background.pages.review.state

import com.motorro.background.pages.review.data.ReviewData
import com.motorro.background.pages.review.data.ReviewUiState
import com.motorro.background.pages.review.net.ReviewApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class UploadingState(
    context: ReviewContext,
    private val data: ReviewData,
    private val api: ReviewApi
) : ReviewState(context) {
    private val totalSteps = data.photos.size + 1
    private var stepsDone = MutableStateFlow(0)

    override fun doStart() {
        super.doStart()
        render(0)
        stepsDone.onEach(::render).launchIn(stateScope)
        upload()
    }

    private fun upload() = stateScope.launch {
        try {
            coroutineScope {
                val uploadedPhotos = data.photos.map { originalPhoto ->
                    async {
                        d { "Uploading photo $originalPhoto" }
                        val uploadedPhoto = api.uploadPhoto(originalPhoto)
                        stepsDone.update { it + 1 }
                        return@async uploadedPhoto
                    }
                }.awaitAll()

                d { "Uploading review..." }
                api.uploadReview(data.copy(photos = uploadedPhotos))

                stepsDone.update { it + 1 }
                setMachineState(factory.uploadSuccess())
            }
        } catch (e: Throwable) {
            ensureActive()
            w(e) { "Review upload error" }
            setMachineState(factory.uploadError(e, data))
        }
    }

    private fun render(step: Int) {
        setUiState(ReviewUiState.Uploading(step to totalSteps))
    }

    class Factory @Inject constructor(private val api: ReviewApi) {
        operator fun invoke(context: ReviewContext, data: ReviewData) = UploadingState(context, data, api)
    }
}