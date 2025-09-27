package com.motorro.cookbook.appcore.navigation

/**
 * Common proxy state interface
 */
fun interface CommonFlowHost {
    /**
     * Called by child flow when it is finished
     */
    fun onComplete()
}