package com.motorro.flow

import com.motorro.flow.data.Note
import com.motorro.flow.data.getNotesFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetUserNotes(userId: Int?, tags: Set<Int>) {
    val state: Flow<List<Note>> = if(null == userId) {
        flowOf(emptyList())
    } else {
        getNotesFlow(userId, tags)
    }
}