package com.motorro.release.state

import com.motorro.release.data.MainScreenViewState

class TerminatedState(context: MainScreenContext) : BaseMainScreenState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.Terminated)
    }
}