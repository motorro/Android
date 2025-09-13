package com.motorro.statemachine.statemachine

import Screen1Destination
import Screen2Destination
import Screen3Destination
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import composeAppNavGraph

enum class MainDestinations(val route: Any, @field:DrawableRes val icon: Int, @field:StringRes val caption: Int) {
    Screen1(Screen1Destination, R.drawable.screen_1, R.string.title_counter_1),
    Screen2(Screen2Destination, R.drawable.screen_2, R.string.title_state_machine),
    Screen3(Screen3Destination, R.drawable.screen_3, R.string.title_counter_2)
}

@Composable
fun ComposeApp(onFinish: () -> Unit) {
    val startDestination = MainDestinations.Screen1
    val navController = rememberNavController()
    val navGraph = remember {
        navController.composeAppNavGraph(onFinish)
    }
    var selectedDestination by rememberSaveable {
        mutableIntStateOf(startDestination.ordinal)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                MainDestinations.entries.forEachIndexed { index, destination ->
                    NavigationBarItem(
                        selected = selectedDestination == index,
                        onClick = {
                            navController.navigate(route = destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                            selectedDestination = index
                        },
                        icon = {
                            Icon(
                                painter = painterResource(destination.icon),
                                contentDescription = stringResource(destination.caption)
                            )
                        },
                        label = { Text(stringResource(destination.caption)) }
                    )
                }
            }
        }
    ) { contentPadding ->
        NavHost(navController, navGraph, modifier = Modifier.padding(contentPadding))
    }
}