package com.motorro.android.contracts

import android.content.Intent
import android.os.Build

@Suppress("DEPRECATION")
inline fun <reified T> Intent?.getParcelable(key: String): T? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    this?.getParcelableExtra(key, T::class.java)
} else {
    this?.getParcelableExtra(key)
}