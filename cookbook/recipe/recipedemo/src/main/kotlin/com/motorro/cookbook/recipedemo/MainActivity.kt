package com.motorro.cookbook.recipedemo

import android.os.Bundle
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.recipe.RecipeScreen
import com.motorro.cookbook.recipedemo.data.RecipeDemoGesture
import com.motorro.cookbook.recipedemo.data.RecipeDemoViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.Serializable

@Serializable
data object Start

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val model: RecipeDemoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            BackHandler {
                model.process(RecipeDemoGesture.Back)
            }

            CookbookTheme {
                RecipeDemoScreen(
                    state = model.viewState.collectAsStateWithLifecycle().value,
                    onGesture = model::process,
                    onTerminated = { finish() }
                )
            }
        }
    }
}

@Composable
private fun RecipeDemoScreen(
    state: RecipeDemoViewState,
    onGesture: (RecipeDemoGesture) -> Unit,
    onTerminated: () -> Unit
) {
    when(state) {
        RecipeDemoViewState.Starter -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { onGesture(RecipeDemoGesture.ToRecipe) }) {
                    Text("Go to recipe with ID")
                }
                Button(onClick = { onGesture(RecipeDemoGesture.ToPreloadedRecipe) }) {
                    Text("Go to recipe with DATA")
                }
            }
        }
        is RecipeDemoViewState.RecipeFlow -> RecipeScreen(
            viewState = state.child,
            onGesture = { onGesture(RecipeDemoGesture.RecipeFlow(it)) },
        )
        RecipeDemoViewState.Terminated -> LaunchedEffect(state) {
            onTerminated
        }
    }
}