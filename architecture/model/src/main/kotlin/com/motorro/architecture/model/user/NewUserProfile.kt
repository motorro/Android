package com.motorro.architecture.model.user

import kotlinx.serialization.Serializable

/**
 * Profile data to use for registration
 * @property displayName User display name
 * @property countryCode ISO 3166-1 country code
 */
@Serializable
data class NewUserProfile(val displayName: String, val countryCode: CountryCode)