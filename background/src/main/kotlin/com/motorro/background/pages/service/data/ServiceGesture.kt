package com.motorro.background.pages.service.data

import com.motorro.background.timer.data.TimerGesture

sealed class ServiceGesture {
    data class Timer(val child: TimerGesture) : ServiceGesture()

    data object StartService: ServiceGesture()
    data object StopService: ServiceGesture()

    data object BindService: ServiceGesture()
    data object UnbindService: ServiceGesture()
}

