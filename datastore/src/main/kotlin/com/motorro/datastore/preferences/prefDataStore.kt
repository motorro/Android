package com.motorro.datastore.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.motorro.datastore.data.MyPreferences
import kotlinx.datetime.LocalDate

/**
 * Data store for preferences
 */
val Context.prefsDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "prefs",
    corruptionHandler = ReplaceFileCorruptionHandler(
        produceNewData = { emptyPreferences() }
    )
)

// Preferences keys
val KEY_USER_NAME = stringPreferencesKey("user_name")
val KEY_PASSWORD = stringPreferencesKey("password")
val KEY_DOB = longPreferencesKey("dob")
val KEY_DISPLAY_DOB = booleanPreferencesKey("display_dob")

/**
 * Converts MyPreferences to preferences
 */
fun Preferences.toMyPreferences(): MyPreferences = MyPreferences(
    userName = get(KEY_USER_NAME) ?: "",
    password = get(KEY_PASSWORD) ?: "",
    dob = get(KEY_DOB)?.let { LocalDate.fromEpochDays(it) },
    displayDob = get(KEY_DISPLAY_DOB) ?: false
)