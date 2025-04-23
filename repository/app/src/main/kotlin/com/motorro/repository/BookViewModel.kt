package com.motorro.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.core.lce.LceState
import com.motorro.repository.data.BookLceState
import com.motorro.repository.usecase.GetBook
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class BookViewModel(bookId: Uuid, private val getBook: GetBook): ViewModel() {

    val book: StateFlow<BookLceState> = getBook(bookId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LceState.Loading()
    )

    fun refresh() = viewModelScope.launch {
        getBook.refresh()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val bookId: Uuid, private val app: App): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
                return BookViewModel(bookId, app.getBook) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}