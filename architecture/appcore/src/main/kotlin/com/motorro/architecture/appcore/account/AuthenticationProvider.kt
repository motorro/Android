package com.motorro.architecture.appcore.account

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes

/**
 * An interface to provide the authentication
 */
interface AuthenticationProvider {
    /**
     * Provider Icon
     */
    @get:DrawableRes
    val iconResource: Int

    /**
     * Provider title
     */
    @get:StringRes
    val title: Int

    /**
     * Navigation Id for the feature
     */
    @get:IdRes
    val route: Int
}