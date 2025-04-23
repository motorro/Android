package com.motorro.repository

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.motorro.core.lce.LceState
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.repository.data.Book
import com.motorro.repository.data.BookLceState
import com.motorro.repository.databinding.FragmentBookBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

class BookFragment : Fragment(), WithViewBinding<FragmentBookBinding> by BindingHost() {
    private val model: BookViewModel by viewModels {
        BookViewModel.Factory(
            bookId = Uuid.parse(BookFragmentArgs.fromBundle(requireArguments()).bookId),
            app = app()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentBookBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.menu, menu)
                }
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.action_refresh -> {
                            if (model.book.value !is LceState.Loading) {
                                model.refresh()
                            }
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.STARTED
        )

        withBinding {
            btnRefresh.setOnClickListener {
                model.refresh()
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.book.onEach(::processDataState).launchIn(this)
            }
        }
    }

    private fun processDataState(state: BookLceState) = when (state) {
        is LceState.Loading -> withBinding {
            Log.d(TAG, "Loading book...")
            progress.show()
            val book = state.data
            if (null != book) {
                contentGroup.isVisible = true
                displayContent(book)
            } else {
                contentGroup.isVisible = false
            }
            errorGroup.isVisible = false
        }

        is LceState.Content -> withBinding {
            Log.d(TAG, "Books loaded.")
            progress.hide()
            contentGroup.isVisible = true
            displayContent(state.data)
            errorGroup.isVisible = false
        }

        is LceState.Error -> withBinding {
            progress.hide()
            val book = state.data
            if (book != null) {
                Log.w(TAG, "Error loading book. Has cached data", state.error)
                contentGroup.isVisible = true
                displayContent(book)
            } else {
                Log.w(TAG, "Error loading book. No cached data", state.error)
                contentGroup.isVisible = false
                errorGroup.isVisible = true
            }
        }
    }

    private fun displayContent(book: Book) = withBinding {
        Glide.with(this@BookFragment).load(book.cover).into(cover)
        cover.contentDescription = book.title
        title.text = book.title

        if (book.authors.isNotEmpty()) {
            author.isVisible = true
            author.text = book.authors.joinToString()
        } else {
            author.isVisible = false
        }

        if (Instant.DISTANT_FUTURE != book.datePublished) {
            published.isVisible = true
            published.text = book.datePublished.toLocalDateTime(TimeZone.currentSystemDefault()).date.toString()
        } else {
            published.isVisible = false
        }

        if (book.summary.isNotEmpty()) {
            summary.isVisible = true
            summary.text = book.summary
        } else {
            summary.isVisible = false
        }
    }

    companion object {
        const val TAG = "BookFragment"
    }
}