package com.motorro.cookbook.recipelist.data

/**
 * Recipe-list screen flow view-state
 */
sealed class RecipeListViewState {

    /**
     * Loading state
     */
    data object Loading : RecipeListViewState()

    /**
     * Recipe
     * @property state Recipe LCE state
     * @property addEnabled Add button enabled state
     * @property refreshEnabled Refresh button enabled state
     */
    data class Content(
        val state: RecipeListItemLce,
        val addEnabled: Boolean,
        val refreshEnabled: Boolean
    ) : RecipeListViewState()

    /**
     * Terminated state - use to close the screen
     */
    data object Terminated : RecipeListViewState()

    companion object {
        /**
         * Empty initial state
         */
        val EMPTY = Loading
    }
}
