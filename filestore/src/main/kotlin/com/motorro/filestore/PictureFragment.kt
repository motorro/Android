package com.motorro.filestore

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.filestore.databinding.FragmentPictureBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class PictureFragment : Fragment(), WithViewBinding<FragmentPictureBinding> by BindingHost() {

    private val bitmap: Bitmap? get() = PictureFragmentArgs.fromBundle(requireArguments()).bitmap
    private val picUri: Uri? get() = PictureFragmentArgs.fromBundle(requireArguments()).picUri

    private val imageTransformation by lazy {
        RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.rounded_corners))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentPictureBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            Glide.with(this@PictureFragment).run {
                when {
                    bitmap != null -> load(bitmap)
                    picUri != null -> load(picUri)
                    else -> throw IllegalArgumentException("No picture data")
                }
            }.transform(imageTransformation).into(image)

            save.setOnClickListener {
                lifecycleScope.launch {
                    findNavController().navigate(PictureFragmentDirections.previewToSave(
                        ImageWithComment(
                            file = saveTempFile(),
                            comment = comment.text.toString()
                        ))
                    )
                }
            }
        }
    }

    private suspend fun saveTempFile(): Uri {
        val comment = withBinding {
            comment.text.toString()
        }

        val bitmap = this@PictureFragment.bitmap
        val picUri = this@PictureFragment.picUri

        val tempPictureFile = when {
            bitmap != null -> saveBitmap(bitmap)
            picUri != null -> saveBitmap(readBitmap(picUri))
            else -> throw IllegalArgumentException("No picture data")
        }

        ExifInterface(tempPictureFile).run {
            setAttribute(ExifInterface.TAG_USER_COMMENT, comment)
            saveAttributes()
        }

        return Uri.fromFile(tempPictureFile)
    }

    private suspend fun readBitmap(uri: Uri): Bitmap = withContext(Dispatchers.IO) {
        requireContext().contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it)
        }
    }

    private suspend fun saveBitmap(toSave: Bitmap): File = withContext(Dispatchers.IO) {
        val cacheDir = requireContext().cacheDir
        val tempFile = File.createTempFile("pic", ".jpg", cacheDir)

        FileOutputStream(tempFile).use {
            toSave.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }

        return@withContext tempFile
    }
}