package com.motorro.repository

import android.graphics.Rect
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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jetsmarter.smartjets.appcore.viewbinding.createBoundViewHolder
import com.motorro.core.lce.LceState
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.repository.data.BookListLceState
import com.motorro.repository.data.ListBook
import com.motorro.repository.databinding.FragmentBookListBinding
import com.motorro.repository.databinding.VhBookBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid

class BookListFragment : Fragment(), WithViewBinding<FragmentBookListBinding> by BindingHost() {

    private lateinit var adapter: BookAdapter
    private val model: BookListModel by viewModels {
        BookListModel.Factory(app())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindView(container, FragmentBookListBinding::inflate)
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
                            if (model.books.value !is LceState.Loading) {
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

        adapter = BookAdapter(
            onSelect = {
                findNavController().navigate(
                    BookListFragmentDirections.bookListToBook(it.toString())
                )
            }
        )
        withBinding {
            bookList.adapter = adapter
            bookList.addItemDecoration(GridDecoration(
                spanCount = resources.getInteger(R.integer.grid_span),
                spacing = resources.getDimensionPixelSize(R.dimen.margin_grid)
            ))
            btnRefresh.setOnClickListener {
                model.refresh()
            }
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.books.onEach(::processDataState).launchIn(this)
                }
            }
        }
    }

    private fun processDataState(state: BookListLceState) = when (state) {
        is LceState.Loading -> withBinding {
            Log.d(TAG, "Loading books. Book count so far: ${state.data.orEmpty().size}")
            progress.show()
            contentGroup.isVisible = true
            errorGroup.isVisible = false
            adapter.submitList(state.data)
        }

        is LceState.Content -> withBinding {
            Log.d(TAG, "Books loaded. Book count: ${state.data.size}")
            progress.hide()
            contentGroup.isVisible = true
            errorGroup.isVisible = false
            adapter.submitList(state.data)
        }

        is LceState.Error -> withBinding {
            progress.hide()
            val books = state.data
            if (books != null) {
                Log.w(TAG, "Error loading books. Has cached data", state.error)
                contentGroup.isVisible = true
                adapter.submitList(books)
            } else {
                Log.w(TAG, "Error loading books. No cached data", state.error)
                contentGroup.isVisible = false
                errorGroup.isVisible = true
            }
        }
    }

    companion object {
        const val TAG = "BookListFragment"
    }
}

private class BookAdapter(private val onSelect: (bookId: Uuid) -> Unit) : ListAdapter<ListBook, BookAdapter.ViewHolder>(ListBookDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = createBoundViewHolder(parent, VhBookBinding::inflate) {
        ViewHolder(it)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override var binding: VhBookBinding?) : RecyclerView.ViewHolder(binding!!.root), WithViewBinding<VhBookBinding> {
        private val glide = Glide.with(itemView)

        private var bookId: Uuid? = null

        init {
            withBinding {
                root.setOnClickListener {
                    bookId?.let(onSelect)
                }
            }
        }

        fun bind(book: ListBook) = withBinding {
            bookId = book.id

            glide.load(book.cover).into(cover)
            cover.contentDescription = root.context.getString(R.string.desc_cover, book.title)
            title.text = book.title
        }
    }

    private object ListBookDiff : DiffUtil.ItemCallback<ListBook>() {
        override fun areItemsTheSame(oldItem: ListBook, newItem: ListBook): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ListBook, newItem: ListBook): Boolean = oldItem == newItem
    }
}

class GridDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val spacing = Math.round(spacing * parent.context.resources.displayMetrics.density)
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount

        outRect.left = spacing - column * spacing / spanCount
        outRect.right = (column + 1) * spacing / spanCount

        outRect.top = if (position < spanCount) spacing else 0
        outRect.bottom = spacing
    }
}