package com.motorro.background.client

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motorro.background.client.ui.MainScreen
import com.motorro.composecore.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                MainScreen(
                    state = viewModel.uiState.collectAsStateWithLifecycle().value,
                    onGesture = viewModel::process,
                    onTerminated = {
                        finish()
                    }
                )
            }
        }
    }
}
