package com.motorro.cookbook.core.utils

import java.lang.ref.WeakReference


/**
 * Checks a map of weak references and create a new instance if absent
 */
inline fun <K, V> MutableMap<K, WeakReference<V>>.getWeakOrPut(key: K, defaultValue: () -> V): V {
    val value = get(key)?.get()
    return if (null == value) {
        val answer = defaultValue()
        put(key, WeakReference(answer))
        answer
    } else {
        value
    }
}
