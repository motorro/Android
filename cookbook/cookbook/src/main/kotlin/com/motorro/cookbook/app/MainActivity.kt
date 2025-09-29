package com.motorro.cookbook.app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.navigation.auth.AuthenticationApi
import com.motorro.cookbook.appcore.navigation.auth.WithLocalAuthentication
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var authenticationApi: AuthenticationApi

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WithLocalAuthentication(authenticationApi) {
                CookbookTheme {
                    MainScreen(
                        state = viewModel.viewState.collectAsStateWithLifecycle().value,
                        onGesture = viewModel::process,
                        onTerminate = {
                            finish()
                        }
                    )
                }
            }
        }
    }
}