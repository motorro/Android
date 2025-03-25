package com.motorro.sqlite

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import com.motorro.sqlite.db.PhotoDb
import com.motorro.sqlite.db.PhotoDbImpl

class App : Application() {
    /**
     * Database instance
     */
    val db: PhotoDb by lazy {
        PhotoDbImpl(this)
    }
}

fun Activity.requireApp() = application as App
fun Fragment.requireApp() = requireActivity().requireApp()