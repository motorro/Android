package com.motorro.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.core.lce.LceState
import com.motorro.repository.data.BookListLceState
import com.motorro.repository.usecase.GetBookList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BookListModel(private val getBookList: GetBookList): ViewModel() {

    val books: StateFlow<BookListLceState> = getBookList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = LceState.Loading()
    )

    fun refresh() = viewModelScope.launch {
        getBookList.refresh()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val app: App): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BookListModel::class.java)) {
                return BookListModel(app.getBookList) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}