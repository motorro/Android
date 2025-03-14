package com.motorro.filestore

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.filestore.databinding.FragmentSaveBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SaveFragment : Fragment(), WithViewBinding<FragmentSaveBinding> by BindingHost() {
    private val image: ImageWithComment get() = SaveFragmentArgs.fromBundle(requireArguments()).toSave

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentSaveBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            btnShare.setOnClickListener {
                share()
            }
            btnMedia.setOnClickListener {
                lifecycleScope.launch {
                    saveToMediaStore()
                    Snackbar.make(
                        requireActivity(),
                        it,
                        getString(R.string.snack_saved_to_media, image.file.toFile().name),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun share() {
        val contentUri = FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.files_authority),
            image.file.toFile(),
            getString(R.string.title_image_with_exif_comment)
        )

        val intent = Intent(Intent.ACTION_SEND).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            setDataAndType(contentUri, requireContext().contentResolver.getType(contentUri))
            putExtra(Intent.EXTRA_STREAM, contentUri)
            putExtra(Intent.EXTRA_TEXT, image.comment)
        }

        startActivity(Intent.createChooser(intent, getString(R.string.title_image_with_exif_comment)))
    }

    private suspend fun saveToMediaStore() = withContext(Dispatchers.IO) {
        val resolver = requireContext().contentResolver
        val imageCollection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val fileName = image.file.toFile().name
        val fileMimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileName.substringAfterLast('.')
        )

        val imageDetails = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.DESCRIPTION, image.comment)
            put(MediaStore.Images.Media.MIME_TYPE, fileMimeType)
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }

        val imageContentUri = checkNotNull(resolver.insert(imageCollection, imageDetails)) {
            "Failed to create image content uri"
        }

        resolver.openOutputStream(imageContentUri).use { outputStream ->
            image.file.toFile().inputStream().use { inputStream ->
                inputStream.copyTo(outputStream!!)
            }
        }

        imageDetails.clear()
        imageDetails.put(MediaStore.Audio.Media.IS_PENDING, 0)
        resolver.update(imageContentUri, imageDetails, null, null)
    }
}