package com.motorro.fragments.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.motorro.fragments.databinding.VhHeaderOrderBinding

class OrderHeaderAdapter : RecyclerView.Adapter<OrderHeaderAdapter.HeaderViewHolder>() {

    override fun getItemCount(): Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderViewHolder {
        return HeaderViewHolder(VhHeaderOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: HeaderViewHolder, position: Int) = Unit

    class HeaderViewHolder(binding: VhHeaderOrderBinding) : RecyclerView.ViewHolder(binding.root)
}