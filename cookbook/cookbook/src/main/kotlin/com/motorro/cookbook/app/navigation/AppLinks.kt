package com.motorro.cookbook.app.navigation

import android.content.Context
import androidx.core.net.toUri
import androidx.navigation.NavDeepLinkRequest
import com.motorro.cookbook.appcore.R
import com.motorro.cookbook.appcore.navigation.Links
import kotlin.uuid.Uuid

class AppLinks(private val context: Context): Links {
    /**
     * Login screen deep-link
     */
    override fun login() = NavDeepLinkRequest.Builder
        .fromUri(context.getString(R.string.link_login).toUri())
        .build()

    /**
     * Recipe deep-link
     * @param id Recipe ID
     */
    override fun recipe(id: Uuid) = NavDeepLinkRequest.Builder
        .fromUri(context.getString(R.string.link_recipe).replace("{recipeId}", id.toString()).toUri())
        .build()

    /**
     * Add recipe
     */
    override fun addRecipe() = NavDeepLinkRequest.Builder
        .fromUri(context.getString(R.string.link_add_recipe).toUri())
        .build()
}