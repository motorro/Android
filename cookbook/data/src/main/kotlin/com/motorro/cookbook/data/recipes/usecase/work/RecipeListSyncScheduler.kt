package com.motorro.cookbook.data.recipes.usecase.work

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import com.motorro.cookbook.core.log.Logging
import com.motorro.cookbook.domain.session.UserHandler
import com.motorro.cookbook.model.Profile
import javax.inject.Inject

/**
 * Adds and removes periodic recipe sync for logged in users.
 */
class RecipeListSyncScheduler @Inject constructor(private val workManager: WorkManager): UserHandler, Logging {

    override suspend fun onLoggedIn(loggedIn: Profile) {
        i { "Scheduling list sync for user: ${loggedIn.name}..." }

        // Schedule immediate sync
        workManager.enqueueUniqueWork(
            uniqueWorkName = RecipeListWorker.UNIQUE_ONE_SHOT_NAME,
            existingWorkPolicy = ExistingWorkPolicy.KEEP,
            request = RecipeListWorker.buildOneShot()
        )

        // Schedule periodic sync
        workManager.enqueueUniquePeriodicWork(
            uniqueWorkName = RecipeListWorker.UNIQUE_PERIODIC_NAME,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
            request = RecipeListWorker.buildPeriodic()
        )
    }

    override suspend fun onLoggedOut(loggedOut: Profile) {
        i { "Unscheduling list sync for user: ${loggedOut.name}..." }
        workManager.cancelAllWorkByTag(RecipeListWorker.TAG)
    }
}