package com.motorro.background.pages.service.state

import android.content.Context
import javax.inject.Inject

interface ServiceStateFactory {
    fun service(): ServiceState

    class Impl @Inject constructor(context: Context) : ServiceStateFactory {

        private val context = object : ServiceContext {
            override val factory: ServiceStateFactory = this@Impl
            override val appContext: Context = context
        }

        override fun service() = ServiceState(context)
    }
}

