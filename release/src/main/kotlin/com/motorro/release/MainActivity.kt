package com.motorro.release

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motorro.composecore.ui.theme.AppTheme
import com.motorro.release.api.MainScreenUiApi
import com.motorro.release.api.WithLocalPages
import com.motorro.release.ui.MainScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.collections.immutable.ImmutableList
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

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
                        }
                    )
                }
            }
        }
    }
}
