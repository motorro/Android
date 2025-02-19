package com.motorro.fragments.menu.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motorro.fragments.data.ItemId
import com.motorro.fragments.data.ItemTotal
import com.motorro.fragments.databinding.VhItemSelectionBinding

interface ItemCallback {
    fun onAdd(itemId: ItemId)
    fun onRemove(itemId: ItemId)
}

class ItemAdapter(private val callback: ItemCallback) : ListAdapter<ItemTotal, ItemAdapter.ViewHolder>(ItemTotalDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        VhItemSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        callback
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: VhItemSelectionBinding, private val callback: ItemCallback) : RecyclerView.ViewHolder(binding.root) {

        private var id: ItemId? = null

        init {
            binding.plus.setOnClickListener{
                id?.let { callback.onAdd(it) }
            }
            binding.minus.setOnClickListener{
                id?.let { callback.onRemove(it) }
            }
        }

        fun bind(item: ItemTotal) {
            id = item.item.id
            with(binding) {
                name.text = item.item.name
                count.text = item.quantity.toString()
            }
        }
    }
}

object ItemTotalDiff : DiffUtil.ItemCallback<ItemTotal>() {
    override fun areItemsTheSame(oldItem: ItemTotal, newItem: ItemTotal): Boolean = oldItem.item.id == oldItem.item.id
    override fun areContentsTheSame(oldItem: ItemTotal, newItem: ItemTotal): Boolean = oldItem == newItem
}