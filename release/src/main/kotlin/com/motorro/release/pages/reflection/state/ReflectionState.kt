package com.motorro.release.pages.reflection.state

import com.motorro.commonstatemachine.CommonMachineState
import com.motorro.core.log.Logging
import com.motorro.release.pages.reflection.data.ReflectionGesture
import com.motorro.release.pages.reflection.data.ReflectionUiState

class ReflectionState(private val context: ReflectionContext): CommonMachineState<ReflectionGesture, ReflectionUiState>(), ReflectionContext by context, Logging {
    override fun doStart() {
        // Use reflection to instantiate the object
        val className = "com.motorro.release.pages.reflection.data.ReflectionDataProvider"
        val clazz = Class.forName(className)
        val instance = clazz.getDeclaredConstructor().newInstance()
        val method = clazz.getMethod("getMessage")
        setUiState(ReflectionUiState(method.invoke(instance) as String))
    }
}