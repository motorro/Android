package com.motorro.statemachine.statemachine

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import platform.posix.exit

fun MainViewController() = ComposeUIViewController {
    val viewModel = remember { MainViewModel() }
    val uiState by viewModel.uiState.collectAsState()
    App(uiState, viewModel::process) {
        exit(0)
    }
}