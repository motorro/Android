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
 * @param titleRes Title resource
 */
fun Fragment.setTitle(@StringRes titleRes: Int) = setTitle(getString(titleRes))

/**
 * Sets title from child fragment
 * @param title Title
 */
fun Fragment.setTitle(title: String) {
    (parentFragment as? WithTitle)?.setTitle(title)
}