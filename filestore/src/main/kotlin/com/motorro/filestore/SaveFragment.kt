package com.motorro.filestore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.filestore.databinding.FragmentSaveBinding

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
}