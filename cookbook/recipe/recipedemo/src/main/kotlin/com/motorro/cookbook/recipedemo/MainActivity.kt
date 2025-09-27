package com.motorro.cookbook.recipedemo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.navigation.Destination
import com.motorro.cookbook.data.recipes.RECIPES
import com.motorro.cookbook.recipe.recipeGraph
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
data object Start

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val controller = rememberNavController()

            CookbookTheme {
                NavHost(controller, Start) {
                    composable<Start> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            Button(
                                modifier = Modifier.align(Alignment.Center),
                                onClick = {
                                    controller.navigate(Destination.RecipeDestination(RECIPES.first().id.toString()))
                                }
                            ) {
                                Text("Go to recipe")
                            }
                        }
                    }
                    recipeGraph(controller)
                }
            }
        }
    }
}