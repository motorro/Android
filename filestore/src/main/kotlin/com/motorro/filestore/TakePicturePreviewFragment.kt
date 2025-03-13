package com.motorro.filestore

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class TakePicturePreviewFragment : Fragment() {

    private var started: Boolean = false

    private val getPictureContract = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        when (bitmap) {
            null -> {
                // Picture was not taken
                findNavController().navigateUp()
            }
            else -> {
                // Picture was taken
                findNavController().navigate(TakePicturePreviewFragmentDirections.takePreviewToPicture(
                    picUri = null,
                    bitmap = bitmap
                ))
            }
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
        getPictureContract.launch(null)
    }

    companion object {
        private const val STARTED = "STARTED"
    }
}