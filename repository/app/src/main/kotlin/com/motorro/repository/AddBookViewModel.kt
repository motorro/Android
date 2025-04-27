package com.motorro.repository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.motorro.repository.data.AddBookViewState
import com.motorro.repository.data.NewBook
import com.motorro.repository.data.getCover
import com.motorro.repository.usecase.AddBook
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddBookViewModel(private val addBook: AddBook) : ViewModel() {

    private val _viewState = MutableStateFlow<AddBookViewState>(AddBookViewState.Editing(AddBookViewState.Form(), false))
    val viewState = _viewState.asStateFlow()

    private inline fun updateForm(block: AddBookViewState.Form.() -> AddBookViewState.Form) {
        val editState = _viewState.value as? AddBookViewState.Editing ?: return
        val newForm = editState.form.block()
        _viewState.value = editState.copy(
            form = newForm,
            saveEnabled = isValid(newForm)
        )
    }

    private fun isValid(form: AddBookViewState.Form): Boolean {
        return form.title.isNotBlank() && form.authors.isNotBlank() && form.summary.isNotBlank()
    }

    fun nextCover() = updateForm { copy(coverId = (coverId + 1)) }
    fun previousCover() = updateForm { copy(coverId = (coverId - 1).coerceAtLeast(1)) }

    fun updateTitle(title: String) = updateForm { copy(title = title) }

    fun updateAuthors(authors: String) = updateForm { copy(authors = authors) }

    fun updateSummary(summary: String) = updateForm { copy(summary = summary) }

    fun save() {
        if (_viewState.value is AddBookViewState.Loading) return

        val form = _viewState.value.form
        if (isValid(form).not()) return

        viewModelScope.launch {
            _viewState.value = AddBookViewState.Loading(form)
            try {
                addBook(
                    NewBook(
                        title = form.title,
                        authors = form.authors.split(",").map { it.trim() },
                        summary = form.summary,
                        cover = getCover(form.coverId)
                    )
                )
                _viewState.value = AddBookViewState.Saved(form)
            } catch (e: Exception) {
                _viewState.value = AddBookViewState.Error(form)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val app: App): ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AddBookViewModel::class.java)) {
                return AddBookViewModel(app.addBook) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}