package ru.merionet.tasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle

class MainActivity : ComponentActivity() {

    /**
     * Common view-model
     */
    private val model: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Subscribe view-state updates
            val viewState by model.uiState.collectAsStateWithLifecycle(lifecycle)

            MainScreen(
                viewState,
                { finish() },
                {
                    // Update model with user action
                    model.process(it)
                }
            )
        }
    }
}