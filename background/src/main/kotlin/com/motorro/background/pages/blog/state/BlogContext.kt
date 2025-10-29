package com.motorro.background.pages.blog.state

import android.content.Context

interface BlogContext {
    val factory: BlogStateFactory
    val appContext: Context
}