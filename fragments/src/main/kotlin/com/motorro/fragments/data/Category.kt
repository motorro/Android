package com.motorro.fragments.data

import androidx.annotation.DrawableRes

data class Category(
    val id: CategoryId,
    val name: String,
    @field:DrawableRes
    val image: Int,
    val items: List<Item>
)