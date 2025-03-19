package com.motorro.datastore

import android.app.Application
import android.util.Log
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.withBinding
import com.motorro.datastore.data.MyPreferences
import com.motorro.datastore.data.isValid
import com.motorro.datastore.databinding.FragmentPreferencesBinding
import com.motorro.datastore.utils.validateInput
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

/**
 * Common view-model interface for preferences
 */
abstract class PreferencesViewModel(application: Application) : AndroidViewModel(application) {

    private var _currentlySaved = MutableStateFlow(MyPreferences())
    private val _preferences = MutableStateFlow(MyPreferences())
    private var _saveJob: Job? = null

    /**
     * Form view state
     */
    val preferences: StateFlow<MyPreferences> get() = _preferences.asStateFlow()

    /**
     * Save button status
     */
    val saveEnabled: StateFlow<Boolean> = _currentlySaved
        .combine(_preferences) { saved, current -> saved != current && current.isValid() }
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    /**
     * Loads preferences
     */
    protected abstract suspend fun loadPreferences(): MyPreferences?

    init {
        viewModelScope.launch {
            val preferences = loadPreferences() ?: return@launch
            _currentlySaved.value = preferences
            _preferences.value = preferences
        }
    }

    /**
     * Updates user name
     */
    fun updateUserName(userName: String) {
        _preferences.update {
            it.copy(userName = userName)
        }
    }

    /**
     * Updates password
     */
    fun updatePassword(password: String) {
        _preferences.update {
            it.copy(password = password)
        }
    }

    /**
     * Updates date of birth
     */
    fun updateDob(dob: LocalDate) {
        _preferences.update {
            it.copy(dob = dob)
        }
    }

    /**
     * Updates display date of birth
     */
    fun updateDisplayDob(displayDob: Boolean) {
        _preferences.update {
            it.copy(displayDob = displayDob)
        }
    }

    /**
     * Saves preferences
     */
    fun save() {
        viewModelScope.launch {
            _saveJob?.join()
            val saved = _currentlySaved.value
            val toSave = _preferences.value
            if (toSave.isValid() && saved != toSave) {
                Log.d(this::class.simpleName, "Saving preferences: $toSave")
                _currentlySaved.value = toSave
                try {
                    doSave(toSave)
                } catch (e: Throwable) {
                    ensureActive()
                    _currentlySaved.value = saved
                    Log.w(this::class.simpleName, "Saving preferences error: $e")
                }
            }
        }
    }

    /**
     * Saves preferences
     */
    protected abstract suspend fun doSave(preferences: MyPreferences)

    /**
     * Data read from stored preferences
     */
    abstract val loopBack: StateFlow<String>
}

fun <T> T.bindViewModel(viewModel: PreferencesViewModel) where T: Fragment, T: WithViewBinding<FragmentPreferencesBinding> = withBinding {
    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.preferences.collect { viewState ->
                inputUserName.setTextKeepState(viewState.userName)
                inputPassword.setTextKeepState(viewState.password)
                inputDob.setTextKeepState(viewState.dob?.toString() ?: "")
                switchShowDob.isChecked = viewState.displayDob
            }
        }
    }

    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.saveEnabled.collect {
                btnSave.isEnabled = it
            }
        }
    }

    viewLifecycleOwner.lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.loopBack.collect {
                textLoopback.text = it
            }
        }
    }

    inputUserName.addTextChangedListener {
        viewModel.updateUserName(it.toString())
    }
    inputPassword.addTextChangedListener {
        viewModel.updatePassword(it.toString())
    }
    inputDob.validateInput(
        validate = { runCatching { LocalDate.parse(it) }.getOrNull() },
        errorText = { getString(R.string.err_invalid_date, it) },
        onValid = { viewModel.updateDob(it) }
    )
    switchShowDob.setOnCheckedChangeListener { _, isChecked ->
        viewModel.updateDisplayDob(isChecked)
    }

    btnSave.setOnClickListener {
        viewModel.save()
    }
}