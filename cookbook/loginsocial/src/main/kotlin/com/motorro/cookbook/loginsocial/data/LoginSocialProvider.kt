package com.motorro.cookbook.loginsocial.data

import androidx.annotation.DrawableRes
import com.motorro.cookbook.loginsocial.R

/**
 * Mock social buttons
 */
enum class LoginSocialProvider(val providerName: String, @field:DrawableRes val icon: Int) {
    Social("Social network", R.drawable.ic_social),
    Email("Email service", R.drawable.ic_email)
}