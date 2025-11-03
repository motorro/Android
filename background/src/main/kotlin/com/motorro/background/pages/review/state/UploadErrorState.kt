package com.motorro.background.pages.review.state

import com.motorro.background.R
import com.motorro.background.pages.review.data.ReviewData
import com.motorro.background.pages.review.data.ReviewGesture
import com.motorro.background.pages.review.data.ReviewUiState

class UploadErrorState(
    context: ReviewContext,
    private val error: Throwable,
    private val data: ReviewData
) : ReviewState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(ReviewUiState.UploadFailed(error.message ?: appContext.getString(R.string.err_upload_failure)))
    }

    override fun doProcess(gesture: ReviewGesture) {
        when(gesture) {
            is ReviewGesture.Action -> {
                d { "Retrying upload..." }
                setMachineState(factory.uploading(data))
            }
            else -> super.doProcess(gesture)
        }
    }
}