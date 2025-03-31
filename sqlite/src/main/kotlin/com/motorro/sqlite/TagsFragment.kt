package com.motorro.sqlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jetsmarter.smartjets.appcore.viewbinding.createBoundViewHolder
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.sqlite.data.ListTag
import com.motorro.sqlite.databinding.FragmentTagsBinding
import com.motorro.sqlite.databinding.VhTagBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class TagsFragment : Fragment(), WithViewBinding<FragmentTagsBinding> by BindingHost() {

    private lateinit var adapter: TagAdapter

    private val model: TagsViewModel by viewModels {
        TagsViewModel.Factory(
            requireApp()
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentTagsBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = TagAdapter(
            onClick = { tag ->
                findNavController().previousBackStackEntry?.savedStateHandle?.set(
                    RESULT,
                    tag.id
                )
                findNavController().navigateUp()
            },
            onEdit = { tag ->
                findNavController().navigate(TagsFragmentDirections.tagsToTag(tag.id))
            },
            onDelete = { tag -> model.deleteTag(tag.id) }
        )
        withBinding {
            tags.adapter = adapter
            tags.addItemDecoration(SpaceDecoration(resources.getDimensionPixelSize(R.dimen.margin_vertical_small)))

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    supervisorScope {
                        launch {
                            model.tags.collect { ts ->
                                adapter.submitList(ts)
                            }
                        }
                    }
                }
            }
            topAppBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            plus.setOnClickListener {
                findNavController().navigate(TagsFragmentDirections.tagsToTag())
            }
        }
    }

    companion object {
        const val RESULT = "result"
    }
}

private class TagAdapter(
    private val onClick: (tag: ListTag) -> Unit,
    private val onEdit: (tag: ListTag) -> Unit,
    private val onDelete: (tag: ListTag) -> Unit
) : ListAdapter<ListTag, TagAdapter.ViewHolder>(TagDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = createBoundViewHolder(parent, VhTagBinding::inflate) {
        ViewHolder(it)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override var binding: VhTagBinding?) : RecyclerView.ViewHolder(binding!!.root), WithViewBinding<VhTagBinding> {

        private var tag: ListTag? = null
        private fun requireTag() = requireNotNull(tag) {
            "Tag is not set"
        }

        init {
            withBinding {
                delete.setOnClickListener {
                    onDelete(requireTag())
                }
                edit.setOnClickListener {
                    onEdit(requireTag())
                }
                root.setOnClickListener {
                    onClick(requireTag())
                }
            }
        }

        fun bind(t: ListTag) = withBinding {
            this@ViewHolder.tag = t
            val tName = t.name
            name.text = tName
            edit.contentDescription = itemView.context.getString(R.string.desc_edit_tag, tName)
            delete.contentDescription = itemView.context.getString(R.string.desc_delete_tag, tName)
        }
    }

    private object TagDiff : DiffUtil.ItemCallback<ListTag>() {
        override fun areItemsTheSame(oldItem: ListTag, newItem: ListTag): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ListTag, newItem: ListTag): Boolean = oldItem.name == newItem.name
    }
}