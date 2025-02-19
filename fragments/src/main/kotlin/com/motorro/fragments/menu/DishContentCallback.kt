package com.motorro.fragments.menu

import com.motorro.fragments.data.CategoryId

interface DishContentCallback {
    fun onCategorySelected(categoryId: CategoryId)
}