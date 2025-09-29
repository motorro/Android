package com.motorro.cookbook.loginsocial.data

import com.motorro.cookbook.appcore.navigation.auth.AuthGesture

/**
 * Social network login gesture
 */
internal sealed class LoginSocialGesture : AuthGesture{
    data object Back : LoginSocialGesture()
    data object DismissError: LoginSocialGesture()
    data class LoginSocial(val providerId: String) : LoginSocialGesture()
}