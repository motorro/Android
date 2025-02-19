package com.motorro.fragments.menu.dish

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motorro.fragments.data.Category
import com.motorro.fragments.data.CategoryId
import com.motorro.fragments.databinding.VhDishBinding

class DishAdapter(private val onItemClick: (CategoryId) -> Unit) : ListAdapter<Category, DishAdapter.ViewHolder>(CategoryDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        VhDishBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
        onItemClick
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: VhDishBinding, private val onItemClick: (CategoryId) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        private var id: CategoryId? = null

        init {
            binding.root.setOnClickListener {
                id?.let(onItemClick)
            }
        }

        fun bind(category: Category) {
            id = category.id
            with(binding) {
                icon.setImageResource(category.image)
                name.text = category.name
            }
        }
    }
}

object CategoryDiff : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean = oldItem == newItem
}