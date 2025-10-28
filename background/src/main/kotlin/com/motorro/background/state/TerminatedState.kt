package com.motorro.background.state

import com.motorro.background.data.MainScreenViewState

class TerminatedState(context: MainScreenContext) : BaseMainScreenState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.Terminated)
    }
}