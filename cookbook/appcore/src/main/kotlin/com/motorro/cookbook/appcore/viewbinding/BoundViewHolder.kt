package com.motorro.cookbook.appcore.viewbinding

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * View-holder with binding
 */
abstract class BoundViewHolder<VB : ViewBinding>(override var binding: VB?) : RecyclerView.ViewHolder(binding!!.root), WithViewBinding<VB>