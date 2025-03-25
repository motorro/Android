package com.motorro.sqlite

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.motorro.sqlite.data.ListImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ImageListViewModel : ViewModel() {
    /**
     * Image list
     */
    val imageList: Flow<List<ListImage>> = MutableStateFlow(emptyList())

    private val _search: MutableStateFlow<String> = MutableStateFlow("")
    val search: Flow<String> get() = _search.asStateFlow()
    fun setSearch(search: String) {
        _search.value = search
    }

    fun deleteImage(imagePath: Uri) {
        //TODO: Implement image deletion
    }

    class Factory(application: App) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ImageListViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ImageListViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}