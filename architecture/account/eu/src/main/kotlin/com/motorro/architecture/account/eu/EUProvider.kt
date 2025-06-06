package com.motorro.architecture.account.eu

import com.motorro.architecture.appcore.account.AuthenticationProvider

/**
 * Provider for Russian social network
 */
object EUProvider : AuthenticationProvider {
    override val iconResource: Int = R.drawable.eu
    override val title: Int = R.string.title_eu_network
    override val route: Int = R.id.account_eu
}