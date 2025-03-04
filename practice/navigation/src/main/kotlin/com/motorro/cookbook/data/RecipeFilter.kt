package com.motorro.cookbook.data

import android.os.Parcel
import android.os.Parcelable

/**
 * Represents a filter for recipes.
 * @property query Search query
 * @property categories Categories to filter by
 */
data class RecipeFilter(val query: String? = null, val categories: Set<RecipeCategory> = emptySet()): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.createStringArrayList()?.mapTo(mutableSetOf()) { RecipeCategory(it) } ?: emptySet()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(query)
        parcel.writeStringList(categories.map { it.name })
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<RecipeFilter> {
        override fun createFromParcel(parcel: Parcel): RecipeFilter = RecipeFilter(parcel)
        override fun newArray(size: Int): Array<RecipeFilter?> = arrayOfNulls(size)
    }
}