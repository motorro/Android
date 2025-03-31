package com.motorro.sqlite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.sqlite.data.ListTag
import com.motorro.sqlite.db.TagsDb
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TagsViewModel(private val tagsDb: TagsDb) : ViewModel() {

    val tags: StateFlow<List<ListTag>> = tagsDb.getList().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun deleteTag(tagId: Int) = viewModelScope.launch {
        tagsDb.deleteTag(tagId)
    }

    class Factory(private val app: App): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TagsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TagsViewModel(app.db.tagsDb) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}