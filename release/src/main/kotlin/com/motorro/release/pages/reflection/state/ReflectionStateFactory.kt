package com.motorro.release.pages.reflection.state

import javax.inject.Inject

interface ReflectionStateFactory {
    fun reflection(): ReflectionState

    class Impl @Inject constructor() : ReflectionStateFactory {
        private val context = object : ReflectionContext {
            override val factory: ReflectionStateFactory = this@Impl
        }

        override fun reflection(): ReflectionState = ReflectionState(context)
    }
}

