package com.motorro.cookbook.data

import kotlinx.serialization.Serializable

/**
 * User profile
 */
@Serializable
data class Profile(val id: UserId, val name: String)