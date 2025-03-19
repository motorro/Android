package com.motorro.datastore.data

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

/**
 * User preferences
 */
@Serializable
data class MyPreferences(
    val userName: String = "",
    val password: String = "",
    val dob: LocalDate? = null,
    val displayDob: Boolean = false
) {
    override fun toString(): String = buildString {
        append("User: $userName\n")
        append("Password: $password\n")
        append("Date of birth: ${dob?.toString() ?: "Not set"}\n")
        append("Display date of birth: $displayDob")
    }
}

/**
 * Validates preferences
 */
fun MyPreferences.isValid(): Boolean = userName.isNotBlank() && password.isNotBlank()