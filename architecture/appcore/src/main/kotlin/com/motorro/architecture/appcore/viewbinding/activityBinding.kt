package com.motorro.architecture.appcore.viewbinding

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding

/**
 * Binds activity view-binding
 * Put inside `onCreate`
 * See: https://developer.android.com/topic/libraries/view-binding#activities
 * @param inflate Binding inflatet
 */
inline fun <reified VB : ViewBinding, reified WWB : WithViewBinding<VB>> Activity.bindView(inflate: (LayoutInflater) -> VB) {
    val withBinding = this as? WWB ?: throw IllegalArgumentException("Activity ${this.javaClass} is not `WithViewBinding`")
    val binding = inflate(layoutInflater)
    withBinding.binding = binding
    setContentView(binding.root)
}