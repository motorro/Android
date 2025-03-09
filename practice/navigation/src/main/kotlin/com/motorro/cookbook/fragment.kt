package com.motorro.cookbook

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController

/**
 * Subscribes to result from fragment dialog using saved state handle of the current back stack entry
 * @param key Result key
 * @param onResult Callback to handle result
 */
inline fun <reified T> Fragment.subscribeToResult(key: String, crossinline onResult: (T) -> Unit) {

    val currentEntry = checkNotNull(findNavController().currentBackStackEntry) {
        "No current back stack entry"
    }

    val observer = object : DefaultLifecycleObserver {
        override fun onResume(owner: LifecycleOwner) {
            val result = currentEntry.savedStateHandle.get<T>(key)
            if (null != result) {
                onResult(result)
                currentEntry.savedStateHandle.remove<T>(key)
            }
        }
    }

    currentEntry.lifecycle.addObserver(observer)

    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
            currentEntry.lifecycle.removeObserver(observer)
        }
    })
}