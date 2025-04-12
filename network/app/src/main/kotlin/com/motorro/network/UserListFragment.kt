package com.motorro.network

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.jetsmarter.smartjets.appcore.viewbinding.createBoundViewHolder
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.network.CreateUserFragment.Companion.CREATE_RESULT
import com.motorro.network.data.CreateUserData
import com.motorro.network.data.UserListItem
import com.motorro.network.databinding.FragmentUserListBinding
import com.motorro.network.databinding.VhUserBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class UserListFragment : Fragment(), WithViewBinding<FragmentUserListBinding> by BindingHost() {
    private lateinit var adapter: UserAdapter
    private val model: UserListModel by viewModels {
        UserListModel.Factory(app())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindView(container, FragmentUserListBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserAdapter(
            onSelect = {
                findNavController().navigate(
                    UserListFragmentDirections.userListToProfile(it)
                )
            },
            onDelete = { model.deleteUser(it) }
        )
        withBinding {
            userList.adapter = adapter
            userList.addItemDecoration(SpaceDecoration(resources.getDimensionPixelSize(R.dimen.margin_vertical_small)))
            add.setOnClickListener {
                findNavController().navigate(UserListFragmentDirections.userListToCreate())
            }
        }
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    model.users.onEach { adapter.submitList(it) }.launchIn(this)
                }
                launch {
                    model.addEnabled.onEach { withBinding { add.isEnabled = it } }.launchIn(this)
                }
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.addEnabled.onEach { withBinding { add.isEnabled = it } }.launchIn(this)
            }
        }
        subscribeToResult(CREATE_RESULT) { result: CreateUserData ->
            model.createUser(result)
        }
    }
}

private class UserAdapter(
    private val onSelect: (userId: Int) -> Unit,
    private val onDelete: (userId: Int) -> Unit
) : ListAdapter<UserListItem, UserAdapter.ViewHolder>(UserDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = createBoundViewHolder(parent, VhUserBinding::inflate) {
        ViewHolder(it)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override var binding: VhUserBinding?) : RecyclerView.ViewHolder(binding!!.root), WithViewBinding<VhUserBinding> {
        private val glide = Glide.with(itemView)
        private val imageTransformation by lazy {
            MultiTransformation(
                CenterCrop(),
                RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners))
            )
        }

        private var userId: Int = -1

        init {
            withBinding {
                root.setOnClickListener {
                    userId.takeIf { it >= 0 }?.let(onSelect)
                }
            }
        }

        fun bind(ui: UserListItem) = withBinding {
            userId = ui.userId

            val iName = ui.user.name

            glide.load(ui.user.userpic.toString()).transform(imageTransformation).into(image)
            image.contentDescription = iName

            name.text = iName
            delete.contentDescription = itemView.context.getString(R.string.desc_delete_user, iName)
            if (ui.deleteEnabled) {
                delete.visibility = View.VISIBLE
                delete.setOnClickListener {
                    userId.takeIf { it >= 0 }?.let(onDelete)
                }
            } else {
                delete.visibility = View.GONE
                delete.setOnClickListener(null)
            }
        }
    }

    private object UserDiff : DiffUtil.ItemCallback<UserListItem>() {
        override fun areItemsTheSame(oldItem: UserListItem, newItem: UserListItem): Boolean = oldItem.userId == newItem.userId
        override fun areContentsTheSame(oldItem: UserListItem, newItem: UserListItem): Boolean = oldItem == newItem
    }
}

class SpaceDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        outRect.top = if (position > 0) spacing else 0
    }
}