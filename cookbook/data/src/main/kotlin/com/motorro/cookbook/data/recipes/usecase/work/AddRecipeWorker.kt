package com.motorro.cookbook.data.recipes.usecase.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.work.BaseUsecaseWorker
import com.motorro.cookbook.model.Recipe
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.json.Json
import kotlin.uuid.Uuid

@HiltWorker
internal class AddRecipeWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val cookbookApi: CookbookApi
) : BaseUsecaseWorker (appContext, params) {
    companion object Companion {
        const val TAG = "UploadPictureWork"
        private const val PARAM_RECIPE = "recipe"

        /**
         * Puts recipe to data
         */
        private fun Data.Builder.putRecipe(recipe: Recipe): Data.Builder = apply {
            putString(
                key = PARAM_RECIPE,
                value = Json.encodeToString(
                    serializer = Recipe.serializer(),
                    value = recipe
                )
            )
        }

        /**
         * Gets image upload result from data
         */
         fun getRecipe(data: Data): Recipe = Json.decodeFromString(
            deserializer = Recipe.serializer(),
            string = requireNotNull(data.getString(PARAM_RECIPE)) {
                "Missing recipe"
            }
        )

        /**
         * Creates unique work name for this worker
         */
        fun getUniqueWorkName(recipeId: Uuid) = "$TAG:$recipeId"

        /**
         * Builds request to add recipe image
         */
        fun buildRequest(recipe: Recipe) = OneTimeWorkRequestBuilder<AddRecipeWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(TAG)
            .setInputData(
                Data.Builder().putRecipe(recipe).build()
            )
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
    }

    override suspend fun doWork(): Result {
        val recipe = getRecipe(inputData)

        d { "Adding recipe ${recipe.id}..." }
        return cookbookApi.addRecipe(recipe).fold(
            onSuccess = {
                Result.success(Data.Builder().putRecipe(it).build())
            },
            onFailure =  {
                currentCoroutineContext().ensureActive()
                val error = it.toCore()
                w(error) { "Error adding recipe" }
                error.toWorkResult()
            }
        )
    }
}