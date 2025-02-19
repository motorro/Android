package com.motorro.fragments.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motorro.fragments.data.ItemId
import com.motorro.fragments.data.ItemTotal
import com.motorro.fragments.databinding.VhItemOrderBinding

class OrderItemAdapter(private val onDelete: (ItemId) -> Unit) : ListAdapter<ItemTotal, OrderItemAdapter.ViewHolder>(ItemTotalDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        VhItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onDelete
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: VhItemOrderBinding, private val onDelete: (ItemId) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        private var id: ItemId? = null

        init {
            binding.delete.setOnClickListener{
                id?.let(onDelete)
            }
        }

        fun bind(item: ItemTotal) {
            id = item.item.id
            with(binding) {
                name.text = item.item.name
                price.text = item.item.price.toString()
                count.text = item.quantity.toString()
                total.text = item.total.toString()
            }
        }
    }
}

object ItemTotalDiff : DiffUtil.ItemCallback<ItemTotal>() {
    override fun areItemsTheSame(oldItem: ItemTotal, newItem: ItemTotal): Boolean = oldItem.item.id == oldItem.item.id
    override fun areContentsTheSame(oldItem: ItemTotal, newItem: ItemTotal): Boolean = oldItem == newItem
}