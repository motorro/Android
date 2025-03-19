package com.motorro.datastore.preferences

import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.viewModelScope
import com.motorro.datastore.PreferencesViewModel
import com.motorro.datastore.data.MyPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PreferencesPreferencesViewModel(application: Application) : PreferencesViewModel(application) {

    override suspend fun loadPreferences(): MyPreferences =
        getApplication<Application>()
            .prefsDataStore
            .data
            .first()
            .toMyPreferences()

    override suspend fun doSave(preferences: MyPreferences) {
        getApplication<Application>()
            .prefsDataStore
            .edit { store ->
                store[KEY_USER_NAME] = preferences.userName
                store[KEY_PASSWORD] = preferences.password
                if (null != preferences.dob) {
                    store[KEY_DOB] = preferences.dob.toEpochDays()
                } else {
                    store.remove(KEY_DOB)
                }
                store[KEY_DISPLAY_DOB] = preferences.displayDob
            }
    }

    override val loopBack: StateFlow<String> = getApplication<Application>()
        .prefsDataStore
        .data
        .map { it.toMyPreferences().toString() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
}