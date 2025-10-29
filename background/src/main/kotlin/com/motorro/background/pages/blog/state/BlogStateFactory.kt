package com.motorro.background.pages.blog.state

import android.content.Context
import javax.inject.Inject

interface BlogStateFactory {
    fun work(): BlogState

    class Impl @Inject constructor(context: Context) : BlogStateFactory {

        private val context = object : BlogContext {
            override val factory: BlogStateFactory = this@Impl
            override val appContext: Context = context
        }

        override fun work() = BlogState(context)
    }
}

