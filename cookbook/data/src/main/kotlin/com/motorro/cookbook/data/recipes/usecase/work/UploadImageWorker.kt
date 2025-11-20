package com.motorro.cookbook.data.recipes.usecase.work

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.work.BaseUsecaseWorker
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlin.uuid.Uuid

@HiltWorker
internal class UploadImageWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val cookbookApi: CookbookApi
) : BaseUsecaseWorker (appContext, params) {
    companion object Companion {
        const val TAG = "UploadPictureWork"
        private const val PARAM_RECIPE_ID = "recipe_id"
        private const val PARAM_IMAGE_URI = "image_uri"

        /**
         * Builds request to add recipe image
         */
        fun buildRequest(recipeId: Uuid, imageUri: Uri) = OneTimeWorkRequestBuilder<UploadImageWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(TAG)
            .setInputData(
                workDataOf(
                    PARAM_RECIPE_ID to recipeId.toString(),
                    PARAM_IMAGE_URI to imageUri.toString()
                )
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
    }

    override suspend fun doWork(): Result {
        val recipeId = requireNotNull(inputData.getString(PARAM_RECIPE_ID)?.let(Uuid::parse)) {
            "Missing recipe id"
        }
        val imageUri = requireNotNull(inputData.getString(PARAM_IMAGE_URI)?.let(Uri::parse)) {
            "Missing image uri"
        }

        d { "Adding image to recipe $recipeId. Image: $imageUri" }
        return cookbookApi.uploadRecipeImage(recipeId, imageUri).fold(
            onSuccess = {
                Result.success()
            },
            onFailure =  {
                currentCoroutineContext().ensureActive()
                val error = it.toCore()
                w(error) { "Error adding image" }
                error.toWorkResult()
            }
        )
    }
}