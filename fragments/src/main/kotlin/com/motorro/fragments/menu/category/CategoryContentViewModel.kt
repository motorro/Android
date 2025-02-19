package com.motorro.fragments.menu.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import com.motorro.fragments.data.CategoryId
import com.motorro.fragments.data.ItemId
import com.motorro.fragments.data.ItemTotal
import com.motorro.fragments.data.menu

class CategoryContentViewModel(categoryId: CategoryId) : ViewModel() {

    private var _state = MutableLiveData(menu.first { it.id == categoryId }.let { category ->
        CategoryViewState(
            name = category.name,
            items = category.items.map { ItemTotal(it, 0) },
            addEnabled = false
        )
    })
    val state: LiveData<CategoryViewState> get() = _state.distinctUntilChanged()

    fun add(itemId: ItemId) = updateQuantity(itemId) { it + 1 }

    fun remove(itemId: ItemId) = updateQuantity(itemId) { (it - 1).coerceAtLeast(0) }

    fun getResult(): CategoryResult {
        val value = _state.value ?: return CategoryResult(emptyList())

        return CategoryResult(
            value.items.filter { it.quantity > 0 }.map {
                it.item.id to it.quantity
            }
        )
    }

    private inline fun updateQuantity(itemId: ItemId, block: (Int) -> Int) {
        val soFar = checkNotNull(_state.value)
        val newItems = soFar.items.map {
            if (it.item.id == itemId) {
                it.copy(quantity = block(it.quantity))
            } else {
                it
            }
        }
        _state.value = soFar.copy(
            items = newItems,
            addEnabled = newItems.any { it.quantity > 0 }
        )
    }

    class Factory(private val categoryId: CategoryId) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = CategoryContentViewModel(categoryId) as T
    }
}