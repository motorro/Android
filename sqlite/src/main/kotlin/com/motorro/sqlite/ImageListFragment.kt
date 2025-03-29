package com.motorro.sqlite

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.jetsmarter.smartjets.appcore.viewbinding.createBoundViewHolder
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.sqlite.data.ListImage
import com.motorro.sqlite.databinding.FragmentImageListBinding
import com.motorro.sqlite.databinding.VhImageBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class ImageListFragment : Fragment(), WithViewBinding<FragmentImageListBinding> by BindingHost() {

    private lateinit var adapter: ImageAdapter
    private lateinit var capturedBehavior: BottomSheetBehavior<ConstraintLayout>
    private val getPicture = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (null != uri) {
            findNavController().navigate(ImageListFragmentDirections.imageListToAdd(uri))
        }
    }

    private val model: ImageListViewModel by viewModels {
        ImageListViewModel.Factory(requireApp())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindView(container, FragmentImageListBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupFilter()
        withBinding {
            adapter = ImageAdapter(
                onSelect = { imagePath -> /* TODO: Navigate to image */ },
                onDelete = { imagePath ->
                    model.deleteImage(imagePath)
                }
            )
            pictures.adapter = adapter
            pictures.addItemDecoration(SpaceDecoration(resources.getDimensionPixelSize(R.dimen.margin_vertical_small)))

            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    supervisorScope {
                        launch {
                            model.imageList.collect {
                                adapter.submitList(it)
                                if (it.isEmpty()) {
                                    noImages.isVisible = true
                                    pictures.isVisible = false
                                } else {
                                    noImages.isVisible = false
                                    pictures.isVisible = true
                                }
                            }
                        }
                        launch {
                            model.search.collect { s ->
                                search.setTextKeepState(s)
                            }
                        }
                    }
                }
            }

            plus.setOnClickListener {
                getPicture.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            search.doAfterTextChanged {
                model.setSearch(it.toString())
            }
        }
    }

    private fun setupFilter() = withBinding {
        capturedBehavior = BottomSheetBehavior.from(filter)
        capturedBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do nothing
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        plus.hide()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        plus.show()
                    }
                }
            }
        })
    }
}

private class ImageAdapter(
    private val onSelect: (imagePath: Uri) -> Unit,
    private val onDelete: (imagePath: Uri) -> Unit
) : ListAdapter<ListImage, ImageAdapter.ViewHolder>(ListImageDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = createBoundViewHolder(parent, VhImageBinding::inflate) {
        ViewHolder(it)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(override var binding: VhImageBinding?) : RecyclerView.ViewHolder(binding!!.root), WithViewBinding<VhImageBinding> {
        private val glide = Glide.with(itemView)
        private val imageTransformation by lazy {
            MultiTransformation(
                CenterCrop(),
                RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners))
            )
        }

        private var image: ListImage? = null

        init {
            withBinding {
                delete.setOnClickListener {
                    this@ViewHolder.image?.path?.let(onDelete)
                }
                root.setOnClickListener {
                    this@ViewHolder.image?.path?.let(onSelect)
                }
            }
        }

        fun bind(i: ListImage) = withBinding {
            this@ViewHolder.image = i

            val iName = i.name

            glide.load(i.path).transform(imageTransformation).into(image)
            image.contentDescription = iName

            name.text = iName
            dateTimeTaken.text = dateFormat.format(i.dateTimeTaken)
            tags.text = i.tag

            delete.contentDescription = itemView.context.getString(R.string.desc_delete_image, iName)
        }
    }

    private object ListImageDiff : DiffUtil.ItemCallback<ListImage>() {
        override fun areItemsTheSame(oldItem: ListImage, newItem: ListImage): Boolean = oldItem.path == newItem.path
        override fun areContentsTheSame(oldItem: ListImage, newItem: ListImage): Boolean = oldItem == newItem
    }
}