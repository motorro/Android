package com.motorro.sqlite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.sqlite.data.Tag
import com.motorro.sqlite.db.TagsDb
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TagViewModel(private val tagsDb: TagsDb, private val toEdit: Int): ViewModel() {

    private var tagData: MutableStateFlow<Tag> = MutableStateFlow(Tag(toEdit, "", ""))
    private inline fun updateTag(block: Tag.() -> Tag) {
        tagData.value = tagData.value.block()
    }

    init {
        if (0 != toEdit) {
            viewModelScope.launch {
                tagsDb.getTag(toEdit)?.let {
                    updateTag { it }
                }
            }
        }
    }

    val name: StateFlow<Pair<String, Boolean>> = tagData
        .map { it.name }
        .map { name -> name to (true == name.takeIf { it.isNotBlank() }?.let { 0 == toEdit && tagsDb.checkTagExists(it) }) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "" to false)

    fun setName(name: String) {
        updateTag { copy(name = name) }
    }

    val description: StateFlow<String> = tagData
        .map { it.description }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    fun setDescription(description: String) {
        updateTag { copy(description = description) }
    }

    val saveEnabled = name
        .map { (name, found) -> found.not() && name.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val _complete = MutableStateFlow(false)
    val complete: StateFlow<Boolean> get() = _complete.asStateFlow()

    fun save() = viewModelScope.launch {
        tagsDb.upsertTag(tagData.value)
        _complete.value = true
    }

    class Factory(private val app: App, private val toEdit: Int): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TagViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TagViewModel(
                    app.db.tagsDb,
                    toEdit
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}