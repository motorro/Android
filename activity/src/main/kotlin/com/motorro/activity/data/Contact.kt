package com.motorro.activity.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Contact data
 */
@Parcelize
data class Contact(
    val id: Int,
    val name: String,
    val email: String
): Parcelable

/**
 * List of contacts
 */
val contactList = listOf(
    Contact(1, "John Doe", "j.doe@m.com"),
    Contact(2, "Vasily Ivanov", "v.ivanov@m.com")
)