package com.motorro.notifications

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.notifications.api.MainScreenUiApi
import com.motorro.notifications.api.WithLocalPages
import com.motorro.notifications.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<MainActivityViewModel.Factory> { factory ->
                factory.create(intent)
            }
        }
    )

    @Inject
    lateinit var pages: @JvmSuppressWildcards ImmutableList<MainScreenUiApi>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                WithLocalPages(pages) {
                    MainScreen(
                        state = viewModel.uiState.collectAsStateWithLifecycle().value,
                        onGesture = viewModel::process,
                        onTerminated = {
                            finish()
                        },
                        onAction = viewModel::processAction,
                        onDismissAction = viewModel::dismissAction
                    )
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.processIntent(intent)
    }
}
