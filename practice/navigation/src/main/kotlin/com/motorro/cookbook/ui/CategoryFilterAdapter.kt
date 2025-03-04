package com.motorro.cookbook.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.databinding.VhRecipeCategoryFilterBinding

/**
 * Recipe adapter
 */
class CategoryFilterAdapter(private val onToggle: (RecipeCategory) -> Unit) : ListAdapter<Pair<RecipeCategory, Boolean>, CategoryFilterAdapter.CategoryHolder>(RecipeCategoryDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
        return CategoryHolder(
            VhRecipeCategoryFilterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onToggle
        )
    }

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CategoryHolder(private val binding: VhRecipeCategoryFilterBinding, private val onToggle: (RecipeCategory) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        private var category: RecipeCategory? = null

        init {
            binding.name.setOnClickListener {
                category?.let {
                    onToggle(it)
                }
            }
        }

        fun bind(category: Pair<RecipeCategory, Boolean>) = with(binding) {
            this@CategoryHolder.category = category.first
            name.text = category.first.name
            name.isChecked = category.second
        }
    }
}

private object RecipeCategoryDiff : DiffUtil.ItemCallback<Pair<RecipeCategory, Boolean>>() {
    override fun areItemsTheSame(oldItem: Pair<RecipeCategory, Boolean>, newItem: Pair<RecipeCategory, Boolean>): Boolean = oldItem.first.name == newItem.first.name
    override fun areContentsTheSame(oldItem: Pair<RecipeCategory, Boolean>, newItem: Pair<RecipeCategory, Boolean>): Boolean = oldItem == newItem
}