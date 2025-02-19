package com.motorro.fragments.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.motorro.fragments.databinding.VhFooterOrderBinding

class OrderFooterAdapter : RecyclerView.Adapter<OrderFooterAdapter.FooterViewHolder>() {

    private var total = 0

    fun setTotal(total: Int) {
        this.total = total
        notifyItemChanged(0)
    }

    override fun getItemCount(): Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FooterViewHolder {
        return FooterViewHolder(VhFooterOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: FooterViewHolder, position: Int) {
        holder.bind(total)
    }

    class FooterViewHolder(private val binding: VhFooterOrderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(total: Int) {
            binding.total.text = total.toString()
        }
    }
}