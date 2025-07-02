package com.motorro.cookbook.appcore.viewbinding

import androidx.core.view.isVisible
import androidx.viewbinding.ViewBinding

/**
 * Component with view-binding
 */
interface WithViewBinding<VB : ViewBinding> {
    /**
     * Component view binding
     */
    var binding: VB?
}

/**
 * Binding storage delegate
 */
class BindingHost<VB : ViewBinding> : WithViewBinding<VB> {
    /**
     * Component view binding
     */
    override var binding: VB? = null
}

/**
 * Runs [block] on binding
 */
inline fun <VB : ViewBinding, R> WithViewBinding<VB>.withBinding(block: VB.() -> R) : R {
    val validBinding: VB = binding ?: throw IllegalStateException("Binding is not initialized!")
    return validBinding.block()
}

/**
 * Marshals property to root layout
 */
var ViewBinding.isVisible: Boolean
    get() = root.isVisible
    set(value) { root.isVisible = value}