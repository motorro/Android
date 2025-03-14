package com.motorro.filestore

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.io.File

class TakePictureFragment : Fragment() {

    private var started: Boolean = false
    private var picUri: Uri? = null

    private val getPictureContract = registerForActivityResult(ActivityResultContracts.TakePicture()) { saved ->
        if (saved) {
            // Picture was taken
            findNavController().navigate(TakePictureFragmentDirections.takePictureToPicture(
                picUri = picUri,
                bitmap = null
            ))
        } else {
            // Picture was not taken
            findNavController().navigateUp()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(STARTED, started)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        started = savedInstanceState?.getBoolean(STARTED) ?: false
    }

    override fun onStart() {
        super.onStart()
        if (!started) {
            started = true
            takePicture()
        }
    }

    private fun takePicture() {
        val cacheDir = requireContext().cacheDir
        val tempFile = File.createTempFile("pic", ".jpg", cacheDir)

        picUri = FileProvider.getUriForFile(
            requireContext(),
            getString(R.string.files_authority),
            tempFile
        )

        getPictureContract.launch(picUri)
    }

    companion object {
        private const val STARTED = "STARTED"
    }
}