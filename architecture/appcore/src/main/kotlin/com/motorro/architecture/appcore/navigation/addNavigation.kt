package com.motorro.architecture.appcore.navigation

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.navigation.NavController

/**
 * Adds a part of navigation at runtime
 * @param navigationId Graph navigation id
 */
@IdRes
fun NavController.addNavigation(@NavigationRes navigationId: Int): Int {
    val newGraph = navInflater.inflate(navigationId)
    graph.addDestination(newGraph)
    return newGraph.id
}