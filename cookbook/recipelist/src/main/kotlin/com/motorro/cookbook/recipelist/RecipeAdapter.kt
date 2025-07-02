package com.motorro.cookbook.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.motorro.cookbook.recipelist.data.RecipeListItem
import com.motorro.cookbook.recipelist.databinding.VhRecipeCategoryBinding
import com.motorro.cookbook.recipelist.databinding.VhRecipeItemBinding
import kotlin.uuid.Uuid
import com.motorro.cookbook.appcore.R as CR

/**
 * Recipe adapter
 */
class RecipeAdapter(private val onClick: (Uuid) -> Unit) : ListAdapter<RecipeListItem, RecipeAdapter.RecipeViewHolder>(RecipeDiff) {

    override fun getItemViewType(position: Int): Int {
        return getItem(position).layoutId
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
        RecipeListItem.RecipeItem.layoutId -> RecipeViewHolder.RecipeHolder(
            VhRecipeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
        RecipeListItem.CategoryItem.layoutId -> RecipeViewHolder.CategoryHolder(
            VhRecipeCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        else -> throw IllegalArgumentException("Unknown view type: $viewType")
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        when(holder) {
            is RecipeViewHolder.CategoryHolder -> holder.bind(getItem(position) as RecipeListItem.CategoryItem)
            is RecipeViewHolder.RecipeHolder -> holder.bind(getItem(position) as RecipeListItem.RecipeItem)
        }
    }

    sealed class RecipeViewHolder(view: View): ViewHolder(view) {
        class RecipeHolder(private val binding: VhRecipeItemBinding, private val onClick: (Uuid) -> Unit) : RecipeViewHolder(binding.root) {

            private val glide = Glide.with(itemView)
            private val imageTransformation by lazy {
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(itemView.context.resources.getDimensionPixelSize(CR.dimen.rounded_corners))
                )
            }

            private lateinit var id: Uuid

            init {
                binding.root.setOnClickListener { onClick(id) }
            }

            fun bind(recipe: RecipeListItem.RecipeItem) = with(binding) {
                this@RecipeHolder.id = recipe.id
                title.text = recipe.title

                val source = recipe.imageUrl?.url ?: CR.drawable.ic_no_image
                glide.load(source)
                    .fallback(CR.drawable.ic_no_image)
                    .placeholder(CR.drawable.ic_no_image)
                    .transform(imageTransformation)
                    .into(image)
                image.contentDescription = recipe.title
            }
        }

        class CategoryHolder(private val binding: VhRecipeCategoryBinding) : RecipeViewHolder(binding.root) {
            fun bind(category: RecipeListItem.CategoryItem) = with(binding) {
                name.text = category.name
            }
        }
    }
}

private object RecipeDiff : DiffUtil.ItemCallback<RecipeListItem>() {
    override fun areItemsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean = oldItem.isSame(newItem)
    override fun areContentsTheSame(oldItem: RecipeListItem, newItem: RecipeListItem): Boolean = oldItem.isContentSame(newItem)
}