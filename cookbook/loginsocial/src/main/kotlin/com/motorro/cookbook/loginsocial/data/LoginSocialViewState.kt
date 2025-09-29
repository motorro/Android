package com.motorro.cookbook.loginsocial.data

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Immutable
import com.motorro.cookbook.appcore.navigation.auth.AuthViewState

/**
 * View-state for social login form
 */
internal sealed class LoginSocialViewState : AuthViewState {
    data object Loading : LoginSocialViewState()
    @Immutable
    data class Buttons(val buttons: List<Button>, val error: String? = null) : LoginSocialViewState() {
        data class Button(val id: String, val title: String, @field:DrawableRes val icon: Int)
    }
    data class LoggingIn(val providerTitle: String, @field:DrawableRes val providerIcon: Int) : LoginSocialViewState()

    companion object {
        /**
         * Empty state
         */
        val EMPTY = Loading
    }
}