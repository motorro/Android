package com.motorro.cookbook.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.navigation.Destination
import com.motorro.cookbook.login.loginGraph
import com.motorro.cookbook.recipe.recipeGraph
import com.motorro.cookbook.recipelist.recipeListGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CookbookTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Destination.RecipeListDestination) {
                    recipeListGraph(navController)
                    recipeGraph(navController)
                    loginGraph(navController)
                }
            }
        }
    }
}