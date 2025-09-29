package com.motorro.cookbook.app.state

import com.motorro.cookbook.app.data.CookbookViewState

/**
 * App termination
 */
class TerminatedState : CookbookState() {
    override fun doStart() {
        super.doStart()
        setUiState(CookbookViewState.Terminated)
    }
}