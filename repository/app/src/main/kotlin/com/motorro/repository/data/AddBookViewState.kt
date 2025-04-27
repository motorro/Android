package com.motorro.repository.data

sealed class AddBookViewState {
    data class Form(
        val title: String = "",
        val authors: String = "",
        val summary: String = "",
        val coverId: Int = 1
    )

    abstract val form: Form
    abstract val saveEnabled: Boolean

    data class Editing(override val form: Form, override val saveEnabled: Boolean) : AddBookViewState()

    data class Loading(override val form: Form) : AddBookViewState() {
        override val saveEnabled: Boolean = false
    }

    data class Error(override val form: Form) : AddBookViewState() {
        override val saveEnabled: Boolean = false
    }

    data class Saved(override val form: Form) : AddBookViewState() {
        override val saveEnabled: Boolean = false
    }
}

fun getCover(coverId: Int): String = "https://picsum.photos/id/${coverId}/200/300"