package com.motorro.flow

import com.motorro.flow.data.Note
import com.motorro.flow.data.getNotesFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

@OptIn(ExperimentalCoroutinesApi::class)
class GetUserNotes(userId: Int?) {

    /**
     * Keeps tags to filter notes
     */
    private val tagsFlow = MutableStateFlow(emptySet<Int>())

    fun setTags(tags: Set<Int>) {
        tagsFlow.value = tags
    }

    val state: Flow<List<Note>> = if(null == userId) {
        flowOf(emptyList())
    } else {
        // For each tag change get notes
        tagsFlow.flatMapLatest { tags ->
            getNotesFlow(userId, tags)
        }
    }
}