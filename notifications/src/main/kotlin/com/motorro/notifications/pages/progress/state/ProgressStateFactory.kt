package com.motorro.notifications.pages.progress.state

import android.app.Application
import javax.inject.Inject

interface ProgressStateFactory {
    fun checkPromo(): ProgressState
    fun stopped(): ProgressState
    fun playing(progress: Int = 0): ProgressState

    class Impl @Inject constructor(app: Application) : ProgressStateFactory {
        private val context = object : ProgressContext {
            override val factory: ProgressStateFactory = this@Impl
            override val androidContext = app.applicationContext
        }

        override fun checkPromo() = CheckingPromoState(context)
        override fun stopped() = StoppedState(context)
        override fun playing(progress: Int) = PlayingState(context, progress)
    }
}