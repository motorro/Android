package com.motorro.architecture.appcore.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * Creates view-holder with view-binding
 * Put inside `onCreateViewHolder`
 * @param parent Parent view-group
 * @param inflate Binding inflater
 * @param create View-holder factory
 */
inline fun <reified VH : RecyclerView.ViewHolder, reified VB : ViewBinding> createBoundViewHolder(parent: ViewGroup, inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB, create: (VB) -> VH): VH {
    val layoutInflater = LayoutInflater.from(parent.context)
    val binding = inflate(layoutInflater, parent, false)
    return create(binding)
}
