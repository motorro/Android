package com.motorro.fragments.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Item ID
 * @param id Item ID
 */
@JvmInline
@Parcelize
value class ItemId(val id: Int): Parcelable