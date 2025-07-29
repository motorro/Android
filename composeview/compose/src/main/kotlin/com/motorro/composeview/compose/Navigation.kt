package com.motorro.composeview.compose

import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.compose.composable
import androidx.navigation.createGraph
import com.motorro.composeview.appcore.timer.model.ListViewModel
import com.motorro.composeview.compose.ui.list.TimerListScreen
import com.motorro.composeview.compose.ui.timer.TimerScreen
import com.motorro.composeview.compose.ui.timer.TimerViewModel
import kotlinx.serialization.Serializable

@Serializable
data object TimerDestination

@Serializable
data object ListDestination

fun NavController.composeAppNavGraph(): NavGraph = createGraph(TimerDestination) {
    composable<TimerDestination> {
        val viewModel: TimerViewModel = hiltViewModel()
        TimerScreen(viewModel)
    }

    composable<ListDestination> {
        val viewModel: ListViewModel = hiltViewModel()
        val timers by viewModel.viewStates.collectAsStateWithLifecycle()
        TimerListScreen(timers, viewModel::process)
    }
}

