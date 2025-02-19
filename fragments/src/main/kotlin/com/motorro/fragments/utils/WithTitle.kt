package com.motorro.fragments.utils

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

/**
 * Interface to set title from child fragments
 */
interface WithTitle {
    /**
     * Sets title
     */
    fun setTitle(title: String)
}

/**
 * Sets title from child fragment
 */
fun Fragment.setTitle(@StringRes titleRes: Int) {
    (parentFragment as? WithTitle)?.setTitle(getString(titleRes))
}