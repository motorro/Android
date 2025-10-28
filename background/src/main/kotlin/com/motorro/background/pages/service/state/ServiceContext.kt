package com.motorro.background.pages.service.state

import android.content.Context

interface ServiceContext {
    val factory: ServiceStateFactory
    val appContext: Context
}