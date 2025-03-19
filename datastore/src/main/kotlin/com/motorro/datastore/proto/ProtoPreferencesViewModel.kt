package com.motorro.datastore.proto

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.motorro.datastore.PreferencesViewModel
import com.motorro.datastore.data.MyPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ProtoPreferencesViewModel(application: Application) : PreferencesViewModel(application) {

    override suspend fun loadPreferences(): MyPreferences =
        getApplication<Application>()
            .protoDataStore
            .data
            .first()

    override suspend fun doSave(preferences: MyPreferences) {
        getApplication<Application>()
            .protoDataStore
            .updateData { stored ->
                stored.copy(
                    userName = preferences.userName,
                    password = preferences.password,
                    dob = preferences.dob,
                    displayDob = preferences.displayDob
                )
            }
    }

    override val loopBack: StateFlow<String> = getApplication<Application>()
        .protoDataStore
        .data
        .map { it.toString() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")
}