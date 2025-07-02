package com.motorro.cookbook.appcore.viewbinding

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding

/**
 * Binds fragment view-binding
 * Put inside `onCreateView`
 * See: https://developer.android.com/topic/libraries/view-binding#fragments
 * @param container View container
 * @param inflate Binding inflater
 */
inline fun <reified VB : ViewBinding, reified WWB : WithViewBinding<VB>> Fragment.bindView(
    container: ViewGroup?,
    inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
): View {
    val withBinding = this as? WWB
        ?: throw IllegalArgumentException("Fragment ${this.javaClass} is not `WithViewBinding`")
    val binding = inflate(layoutInflater, container, false)
    withBinding.binding = binding
    BindingDestroyer.install(viewLifecycleOwner, withBinding)
    return binding.root
}

/**
 * Destroys binding on view destroy
 */
class BindingDestroyer<VB : ViewBinding> private constructor(
    private val viewOwner: LifecycleOwner,
    private val withBinding: WithViewBinding<VB>
) : DefaultLifecycleObserver {
    companion object {
        /**
         * Installs binding destroyer
         */
        fun <VB : ViewBinding> install(
            viewOwner: LifecycleOwner,
            withBinding: WithViewBinding<VB>
        ) {
            viewOwner.lifecycle.addObserver(BindingDestroyer(viewOwner, withBinding))
        }
    }

    /**
     * Destroys binding and unregisters
     */
    override fun onDestroy(owner: LifecycleOwner) {
        withBinding.binding = null
        viewOwner.lifecycle.removeObserver(this)
    }
}