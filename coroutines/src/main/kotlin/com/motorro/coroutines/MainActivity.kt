package com.motorro.coroutines

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.motorro.coroutines.databinding.ActivityMainBinding
import com.motorro.coroutines.databinding.ContentBinding
import com.motorro.coroutines.databinding.ErrorBinding
import com.motorro.coroutines.databinding.LoadingBinding
import com.motorro.coroutines.databinding.LoginBinding
import com.motorro.coroutines.network.Api
import com.motorro.coroutines.network.data.LoginRequest
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var login: LoginBinding
    private lateinit var loading: LoadingBinding
    private lateinit var content: ContentBinding
    private lateinit var error: ErrorBinding

    private var viewState: MainActivityViewState by Delegates.observable(MainActivityViewState.Login) { _, _, newValue ->
        when(newValue) {
            is MainActivityViewState.Content -> showContent(newValue)
            MainActivityViewState.Loading -> showLoading()
            MainActivityViewState.Login -> showLogin()
        }
    }

    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        login = binding.loginScreen
        loading = binding.loadingScreen
        content = binding.contentScreen
        error = binding.errorScreen

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupLogin()
        setupLoading()
        setupContent()
        setupError()

        viewState = MainActivityViewState.Login

        api = Api.create()
    }

    // region Logic

    private fun login(username: String, password: String) {
        showLoading()
        log { "Starting login..." }
        try {
            api.login(LoginRequest(username, password), willFailOnLogin()) {
                val user = it.getOrThrow()
                log { "Successfully logged in user: ${user.id}" }
                log { "Loading profile for user: ${user.id}..."}
                api.getProfile(user.token, user.id, willFailOnProfile()) {
                    val profile = it.getOrThrow()
                    log { "Profile loaded for user: ${user.id}" }
                    showContent(MainActivityViewState.Content(profile))
                }
            }
        } catch (e: Throwable) {
            log { "Network failed: ${e.message}" }
            showError(e)
        }
    }

    private fun logout() {
        showLogin()
    }

    private fun returnFromError() {
        showLogin()
    }

    // endregion

    // region View

    private fun setupLogin() {
        login.loginGroup.isVisible = false
        login.loginButton.setOnClickListener {
            val name = login.login.text?.trim() ?: ""
            val password = login.password.text?.trim() ?: ""

            login(name.toString(), password.toString())
        }
    }

    private fun setupLoading() {
        loading.loadingGroup.isVisible = false
    }

    private fun setupContent() {
        content.contentGroup.isVisible = false
        content.logout.setOnClickListener {
            logout()
        }
    }

    private fun setupError() {
        error.errorGroup.isVisible = false
        error.btnReturn.setOnClickListener {
            returnFromError()
        }
    }

    private fun showLogin() {
        login.loginGroup.isVisible = true
        loading.loadingGroup.isVisible = false
        content.contentGroup.isVisible = false
        error.errorGroup.isVisible = false
    }

    private fun showLoading() {
        login.loginGroup.isVisible = false
        loading.loadingGroup.isVisible = true
        content.contentGroup.isVisible = false
        error.errorGroup.isVisible = false
    }

    private fun showContent(state: MainActivityViewState.Content) {
        login.loginGroup.isVisible = false
        loading.loadingGroup.isVisible = false
        error.errorGroup.isVisible = false
        with(content) {
            contentGroup.isVisible = true
            name.text = getString(R.string.name, state.profile.name)
            age.text = getString(R.string.age, state.profile.age)
            registration.text = getString(
                R.string.registration_date,
                state.profile.registered.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            )
        }
    }

    private fun showError(error: Throwable) {
        login.loginGroup.isVisible = false
        loading.loadingGroup.isVisible = false
        content.contentGroup.isVisible = false
        with(this.error) {
            errorGroup.isVisible = true
            errorText.text = error.message ?: error.toString()
        }
    }

    private fun willFailOnLogin(): Boolean = login.failLogin.isChecked

    private fun willFailOnProfile(): Boolean = login.failProfile.isChecked

    // endregion

    private inline fun log(block: () -> String) {
        val activityState = lifecycle.currentState.name
        Log.i("MainActivity", "===== log =====")
        Log.i("MainActivity", "Activity state: $activityState")
        Log.i("MainActivity", block())
    }
}