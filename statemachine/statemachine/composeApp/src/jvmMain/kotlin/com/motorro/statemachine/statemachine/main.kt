package com.motorro.statemachine.statemachine

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {

    val model = MainModel()

    Window(onCloseRequest = ::exitApplication, title = "StateMachine") {
        val uiState by model.uiState.collectAsState()
        App(uiState, model::process) {
            exitApplication()
        }
    }
}