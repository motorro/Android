package com.motorro.datastore.vanilla

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.motorro.datastore.PreferencesViewModel
import com.motorro.datastore.data.MyPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import java.io.File

class VanillaPreferencesViewModel(application: Application) : PreferencesViewModel(application) {

    private val _loopback = MutableStateFlow("")

    init {
        viewModelScope.launch {
            _loopback.value = loadPreferences()?.toString().orEmpty()
        }
    }

    override suspend fun loadPreferences(): MyPreferences? {
        return withContext(Dispatchers.IO) {
            val file = File(getApplication<Application>().filesDir, FILE_NAME)
            if (!file.exists()) {
                return@withContext null
            }
            file.bufferedReader().use { reader ->
                val properties = reader.readLines().associate {
                    val (key, value) = it.split("=")
                    key to value
                }
                MyPreferences(
                    userName = properties[KEY_USER_NAME] ?: "",
                    password = properties[KEY_USER_PASSWORD] ?: "",
                    dob = properties[KEY_DOB]?.toIntOrNull()?.let { LocalDate.fromEpochDays(it) },
                    displayDob = properties[KEY_DISPLAY_DOB]?.toBoolean() ?: false
                )
            }
        }
    }

    override suspend fun doSave(preferences: MyPreferences) {
        withContext(Dispatchers.IO) {
            val file = File(getApplication<Application>().filesDir, FILE_NAME)
            file.printWriter().use { out ->
                out.format("%s=%s\n", KEY_USER_NAME, preferences.userName)
                out.format("%s=%s\n", KEY_USER_PASSWORD, preferences.password)
                if (null != preferences.dob) {
                    out.format("%s=%d\n", KEY_DOB, preferences.dob.toEpochDays())
                }
                out.format("%s=%b\n", KEY_DISPLAY_DOB, preferences.displayDob)
            }
        }
        _loopback.value = preferences.toString()
    }

    override val loopBack: StateFlow<String> = _loopback.asStateFlow()

    companion object {
        private const val FILE_NAME = "vanilla.properties"
        private const val KEY_USER_NAME = "userName"
        private const val KEY_USER_PASSWORD = "password"
        private const val KEY_DOB = "dob"
        private const val KEY_DISPLAY_DOB = "displayDob"
    }
}