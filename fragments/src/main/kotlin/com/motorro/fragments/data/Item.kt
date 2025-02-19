package com.motorro.fragments.data

/**
 * Order item
 * @property id Item ID
 * @property name Item name
 * @property basePrice Base price
 * @property children Child items
 */
data class Item(val id: ItemId, val name: String, val price: Int)

/**
 * Item with quantity
 * @property item Item
 * @property quantity Quantity
 */
data class ItemTotal(val item: Item, val quantity: Int) {
    val total: Int get() = item.price * quantity
}