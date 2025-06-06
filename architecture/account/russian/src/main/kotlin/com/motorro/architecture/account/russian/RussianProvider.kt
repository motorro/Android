package com.motorro.architecture.account.russian

import com.motorro.architecture.appcore.account.AuthenticationProvider

/**
 * Provider for Russian social network
 */
object RussianProvider : AuthenticationProvider {
    override val iconResource: Int = R.drawable.russian
    override val title: Int = R.string.title_russian_network
    override val route: Int = R.id.account_russian
}