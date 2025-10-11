package com.motorro.notifications.state

import com.motorro.notifications.data.MainScreenViewState

class TerminatedState(context: MainScreenContext) : BaseMainScreenState(context) {
    override fun doStart() {
        super.doStart()
        setUiState(MainScreenViewState.Terminated)
    }
}