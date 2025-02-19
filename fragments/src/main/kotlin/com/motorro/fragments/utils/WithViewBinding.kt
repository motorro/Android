package com.motorro.fragments.utils

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
