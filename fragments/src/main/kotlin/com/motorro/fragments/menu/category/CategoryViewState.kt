package com.motorro.fragments.menu.category

import com.motorro.fragments.data.ItemTotal

data class CategoryViewState(
    val name: String,
    val items: List<ItemTotal>,
    val addEnabled: Boolean
)