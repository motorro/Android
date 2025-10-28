package com.motorro.background.pages.service.data

import com.motorro.background.timer.data.TimerGesture

sealed class ServiceGesture {
    data class Timer(val child: TimerGesture) : ServiceGesture()
}