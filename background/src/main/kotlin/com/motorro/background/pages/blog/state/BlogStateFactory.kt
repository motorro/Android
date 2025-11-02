package com.motorro.background.pages.blog.state

import android.content.Context
import javax.inject.Inject
import javax.inject.Provider

interface BlogStateFactory {
    fun work(): BlogState

    class Impl @Inject constructor(context: Context, private val createWorkState: Provider<BlogState.Factory>) : BlogStateFactory {

        private val context = object : BlogContext {
            override val factory: BlogStateFactory = this@Impl
            override val appContext: Context = context
        }

        override fun work() = createWorkState.get().invoke(context)
    }
}

