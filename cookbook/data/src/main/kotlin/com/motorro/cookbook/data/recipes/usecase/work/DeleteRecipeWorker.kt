package com.motorro.cookbook.data.recipes.usecase.work

import android.content.Context
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
internal class DeleteRecipeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val cookbookApi: CookbookApi
) : BaseUsecaseWorker (appContext, params) {
    companion object {
        const val TAG = "DeleteRecipeWork"
        private const val PARAM_RECIPE_ID = "recipe_id"

        /**
         * Creates unique work name for this worker
         */
        fun getUniqueWorkName(recipeId: Uuid) = "$TAG:$recipeId"

        /**
         * Builds request to delete recipe
         */
        fun buildRequest(recipeId: Uuid) = OneTimeWorkRequestBuilder<DeleteRecipeWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(TAG)
            .setInputData(workDataOf(PARAM_RECIPE_ID to recipeId.toString()))
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

        d { "Deleting recipe $recipeId" }
        return cookbookApi.deleteRecipe(recipeId).fold(
            onSuccess = {
                d { "Recipe $recipeId deleted" }
                Result.success()
            },
            onFailure = {
                currentCoroutineContext().ensureActive()
                val error = it.toCore()
                w(error) { "Error deleting recipe" }
                error.toWorkResult()
            }
        )
    }
}