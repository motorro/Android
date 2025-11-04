package com.motorro.background.pages.review.state

import androidx.work.WorkManager
import com.motorro.background.pages.review.PhotoUploadWorker
import com.motorro.background.pages.review.ReviewUploadWorker
import com.motorro.background.pages.review.data.ReviewData

class UploadingState(
    context: ReviewContext,
    private val data: ReviewData
) : ReviewState(context) {
    override fun doStart() {
        super.doStart()
        upload()
    }

    private fun upload() {
        d { "Uploading review..." }

        val workManager = WorkManager.getInstance(appContext)

        val reviewUploadRequest = ReviewUploadWorker.createJob(data.rating, data.text)
        if (data.photos.isEmpty()) {
            d { "No photos. Launching standard request" }
            workManager.enqueue(reviewUploadRequest)
        } else {
            d { "Photos. Launching complex request" }
            workManager
                .beginWith(data.photos.map { PhotoUploadWorker.createJob(it) })
                .then(reviewUploadRequest)
                .enqueue()
        }

        setMachineState(factory.uploadSuccess())
    }
}