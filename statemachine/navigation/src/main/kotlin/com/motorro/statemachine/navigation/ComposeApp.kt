package com.motorro.statemachine.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun ComposeApp() {
    val navController = rememberNavController()
    val navGraph = remember {
        navController.composeAppNavGraph()
    }
    NavHost(navController, navGraph)
}