package com.motorro.fragments.menu.category

import android.os.Parcelable
import com.motorro.fragments.data.ItemId
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryResult(val itemsToAdd: List<Pair<ItemId, Int>>): Parcelable