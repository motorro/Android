package com.motorro.architecture.model.user

import kotlinx.serialization.Serializable

/**
 * User profile
 * @property id User ID
 * @property displayName User display name
 * @property countryCode ISO 3166-1 country code
 */
@Serializable
data class UserProfile(val id: UserId, val displayName: String, val countryCode: CountryCode)