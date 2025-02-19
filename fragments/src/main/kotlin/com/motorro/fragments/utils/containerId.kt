package com.motorro.fragments.utils

import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

@get:IdRes
val Fragment.containerId: Int get() {
    val parent = checkNotNull(view?.parent as? ViewGroup) {
        "Fragment has no parent view"
    }
    return parent.id
}