package com.motorro.cookbook

import androidx.lifecycle.MutableLiveData

/**
 * Updates mutable live data value
 */
inline fun <T: Any> MutableLiveData<T>.update(block: (T) -> T) {
    val soFar = checkNotNull(value) {
        "Mutable live data value is not set"
    }
    val update = block(soFar)
    if (soFar != update) {
        value = update
    }
}
