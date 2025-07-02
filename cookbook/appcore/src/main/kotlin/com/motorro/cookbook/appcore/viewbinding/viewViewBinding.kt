package com.motorro.cookbook.appcore.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding

/**
 * Binds view view-binding
 * Put inside `init`
 * See: https://developer.android.com/topic/libraries/view-binding#fragments
 * @param container View container
 * @param inflate Binding inflater
 */
inline fun <reified VB : ViewBinding, reified WWB : WithViewBinding<VB>> View.bindView(container: ViewGroup?, inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB): View {
    val withBinding = this as? WWB ?: throw IllegalArgumentException("View ${this.javaClass} is not `WithViewBinding`")
    val binding = inflate(LayoutInflater.from(context), container, false)
    withBinding.binding = binding
    return binding.root
}

/**
 * Binds view-group view-binding adding inflated view
 * Put inside `init`
 * See: https://developer.android.com/topic/libraries/view-binding#fragments
 * @param inflate Binding inflater
 */
inline fun <reified VB : ViewBinding, reified WWB : WithViewBinding<VB>> ViewGroup.bindView(inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB) {
    addView(bindView<VB, WWB>(this, inflate))
}
