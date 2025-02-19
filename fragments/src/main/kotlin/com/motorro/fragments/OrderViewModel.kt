package com.motorro.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.motorro.fragments.data.ItemId
import com.motorro.fragments.data.ItemTotal
import com.motorro.fragments.data.menu

data class Order(val items: List<ItemTotal>) {
    val total: Int get() = items.sumOf { it.total }
}

class OrderViewModel : ViewModel() {

    private val items = menu.flatMap { it.items }.associateBy { it.id }

    private val _contents: MutableLiveData<Map<ItemId, Int>> = MutableLiveData(emptyMap())
    val contents: LiveData<Order> get() = _contents.distinctUntilChanged().map {
        Order(it.map { (itemId, quantity) -> ItemTotal(items.getValue(itemId), quantity) })
    }

    fun add(itemId: ItemId, quantity: Int) {
        _contents.value = checkNotNull(_contents.value).let { soFar ->
            soFar + (itemId to soFar.getOrDefault(itemId, 0) + quantity)
        }
    }

    fun remove(itemId: ItemId) {
        _contents.value = checkNotNull(_contents.value) - itemId
    }
}