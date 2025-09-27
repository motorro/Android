package com.motorro.cookbook.recipelist.state

import com.motorro.cookbook.domain.session.SessionManager
import com.motorro.cookbook.recipelist.data.RecipeListViewState
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Logs out
 */
internal class LoggingOutState(
    context: RecipeListContext,
    private val sessionManager: SessionManager
) : RecipeListState(context) {

    override fun doStart() {
        super.doStart()
        setUiState(RecipeListViewState.Loading)
        logout()
    }

    /**
     * Deletes recipe
     */
    private fun logout() = stateScope.launch {
        d { "Logging-out..." }
        sessionManager.logout()
        // Start from scratch
        setMachineState(factory.init())
    }

    /**
     * Factory for [LoggingOutState]
     */
    class Factory @Inject constructor(private val sessionManager: SessionManager) {
        operator fun invoke(context: RecipeListContext) = LoggingOutState(
            context,
            sessionManager
        )
    }
}