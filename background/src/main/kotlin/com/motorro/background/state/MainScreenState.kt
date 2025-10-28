package com.motorro.background.state

import androidx.annotation.CallSuper
import com.motorro.background.data.MainScreenGesture
import com.motorro.background.data.MainScreenViewState
import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.core.log.Logging

typealias MainScreenState = CommonMachineState<MainScreenGesture, MainScreenViewState>

abstract class BaseMainScreenState(context: MainScreenContext): MainScreenState(), Logging, MainScreenContext by context {
    @CallSuper
    override fun doStart() {
        d { "Starting state: ${javaClass.simpleName}" }
    }

    override fun doProcess(gesture: MainScreenGesture) {
        w { "Gesture not handled: $gesture" }
    }

    @CallSuper
    override fun doClear() {
        super.doClear()
        d { "Clearing state: ${javaClass.simpleName}" }
    }
}