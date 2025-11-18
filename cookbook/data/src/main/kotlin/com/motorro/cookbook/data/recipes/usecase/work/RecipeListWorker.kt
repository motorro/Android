package com.motorro.cookbook.data.recipes.usecase.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.motorro.cookbook.core.error.toCore
import com.motorro.cookbook.data.recipes.CookbookApi
import com.motorro.cookbook.data.recipes.db.CookbookDao
import com.motorro.cookbook.data.recipes.db.entity.toEntity
import com.motorro.cookbook.data.work.BaseUsecaseWorker
import com.motorro.cookbook.data.work.toErrorData
import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.domain.session.requireUserId
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import java.time.Duration

@HiltWorker
internal class RecipeListWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters,
    private val sessionManager: SessionManager,
    private val cookbookDao: CookbookDao,
    private val cookbookApi: CookbookApi
) : BaseUsecaseWorker (appContext, params) {
    companion object {
        const val TAG = "RecipeListWork"
        const val UNIQUE_ONE_SHOT_NAME = "SyncRecipeList"
        const val UNIQUE_PERIODIC_NAME = "SyncRecipeListPeriodic"
        private val SYNC_PERIOD = Duration.ofHours(1)

        /**
         * Builds one-shot sync request for recipe list
         */
        fun buildOneShot() = OneTimeWorkRequestBuilder<RecipeListWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .addTag(TAG)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
    }

    override suspend fun doWork(): Result {
        d { "Starting recipe list sync..." }

        val userId = try {
            sessionManager.requireUserId()
        } catch (e: Throwable) {
            w(e) { "No active session. Failure..." }
            return Result.failure()
        }

        return cookbookApi.getRecipeList().fold(
            onSuccess = { recipes ->
                d { "Recipes received" }
                cookbookDao.insertList(recipes.map { it.toEntity(userId) })
                return@fold Result.success()
            },
            onFailure = {
                currentCoroutineContext().ensureActive()
                val error = it.toCore()
                w (it.toCore()) { "Error getting recipes" }
                return@fold Result.failure(error.toErrorData())
            }
        )
    }
}