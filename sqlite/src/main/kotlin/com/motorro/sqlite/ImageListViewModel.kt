package com.motorro.sqlite

import android.net.Uri
import androidx.core.net.toFile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.sqlite.data.ListImage
import com.motorro.sqlite.data.ListTag
import com.motorro.sqlite.data.PhotoFilter
import com.motorro.sqlite.db.PhotoDb
import com.motorro.sqlite.db.TagsDb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalCoroutinesApi::class)
class ImageListViewModel(private val db: PhotoDb, private val tagsDb: TagsDb) : ViewModel() {

    private val _filter: MutableStateFlow<PhotoFilter> = MutableStateFlow(PhotoFilter())

    /**
     * Image list
     */
    val imageList: StateFlow<List<ListImage>> = _filter
        .flatMapLatest { filter ->
            db.getList(filter)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val search: StateFlow<String> = _filter
        .map { it.name.orEmpty() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    fun setSearch(search: String) {
        _filter.update {
            it.copy(name = search.takeIf { it.isNotBlank() })
        }
    }

    fun toggleTag(tagId: Int) {
        _filter.update {
            it.copy(tags = if (tagId in it.tags) it.tags - tagId else it.tags + tagId)
        }
    }

    val tags: StateFlow<List<Pair<ListTag, Boolean>>> = tagsDb.getList()
        .combine(_filter.map { it.tags }) { tags, selectedTags -> tags.map { it to (it.id in selectedTags) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deleteImage(imagePath: Uri) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            imagePath.toFile().delete()
        }
        db.deleteImage(imagePath)
    }

    class Factory(private val application: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ImageListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ImageListViewModel(application.db, application.db.tagsDb) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}