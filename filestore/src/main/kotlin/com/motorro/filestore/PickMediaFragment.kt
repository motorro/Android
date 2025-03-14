package com.motorro.filestore

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import com.motorro.filestore.databinding.FragmentPickMediaBinding
import com.motorro.filestore.databinding.VhImageBinding
import kotlin.properties.Delegates

class PickMediaFragment : Fragment(), WithViewBinding<FragmentPickMediaBinding> by BindingHost() {
    private lateinit var resolver: ContentResolver
    private lateinit var adapter: ImageCursorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentPickMediaBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resolver = requireContext().contentResolver
        adapter = ImageCursorAdapter(
            ::getCaption,
            ::getImageUri
        ) {
            findNavController().navigate(PickMediaFragmentDirections.pickMediaToPicture(
                picUri = it,
                bitmap = null
            ))
        }

        withBinding {
            pictures.adapter = adapter
            pictures.addItemDecoration(
                GridDecoration(
                    spanCount = resources.getInteger(R.integer.grid_span),
                    spacing = resources.getDimensionPixelSize(R.dimen.grid_margin)
                )
            )
        }

        loadPictures()
    }

    private fun getCaption(mediaCursor: Cursor): String {
        val nameColumn = mediaCursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
        return mediaCursor.getString(nameColumn)
    }

    private fun getImageUri(mediaCursor: Cursor): Uri {
        val idColumn = mediaCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val id = mediaCursor.getLong(idColumn)
        return ContentUris.withAppendedId(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            id
        )
    }

    private fun loadPictures() {
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
        )

        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"

        @SuppressLint("Recycle")
        val query = resolver.query(
            imageCollection,
            projection,
            null,
            null,
            sortOrder
        )

        when {
            null == query -> adapter.resetCursor(null)
            0 == query.count -> {
                adapter.resetCursor(null)
                query.close()
            }
            else -> adapter.resetCursor(query)
        }
    }
}

private class GridDecoration(private val spanCount: Int, private val spacing: Int) : RecyclerView.ItemDecoration() {
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

@SuppressLint("NotifyDataSetChanged")
private class ImageCursorAdapter(
    private val getTitle: (Cursor) -> String,
    private val getThumbnailUri: (Cursor) -> Uri,
    private val onSelect: (Uri) -> Unit
) : RecyclerView.Adapter<ImageCursorAdapter.ViewHolder>() {

    private var cursor: Cursor? by Delegates.observable(null) { _, old, new ->
        old?.close()
        notifyDataSetChanged()
    }

    fun resetCursor(cursor: Cursor?) {
        this.cursor = cursor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = createBoundViewHolder(parent, VhImageBinding::inflate) {
        ViewHolder(it)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cursor = checkNotNull(this.cursor) {
            "Cursor is not set"
        }
        check(cursor.moveToPosition(position)) {
            "Failed to move cursor to position $position"
        }
        holder.bind(cursor)
    }

    override fun getItemCount(): Int = cursor?.count ?: 0

    inner class ViewHolder(override var binding: VhImageBinding?) : RecyclerView.ViewHolder(binding!!.root), WithViewBinding<VhImageBinding> {
        private val glide = Glide.with(itemView)
        private val imageTransformation by lazy {
            MultiTransformation(
                CenterCrop(),
                RoundedCorners(itemView.context.resources.getDimensionPixelSize(R.dimen.rounded_corners))
            )
        }

        private var uri: Uri? = null

        init {
            withBinding {
                root.setOnClickListener {
                    uri?.let(onSelect)
                }
            }
        }

        fun bind(cursor: Cursor) = withBinding {
            val uri = getThumbnailUri(cursor)
            this@ViewHolder.uri = uri

            glide.load(uri).transform(imageTransformation).into(image)
            name.text = getTitle(cursor)
        }
    }
}
