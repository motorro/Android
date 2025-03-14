package com.motorro.filestore

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ProviderFragment : Fragment() {

    private var started: Boolean = false

    private val getPictureContract = registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
        if (null != uri) {
            // Picture was selected
            findNavController().navigate(ProviderFragmentDirections.provideMediaToPicture(
                picUri = uri,
                bitmap = null
            ))
        } else {
            // Picture was not selected
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
            selectPicture()
        }
    }

    private fun selectPicture() {
        getPictureContract.launch(arrayOf("image/*"))
    }

    companion object {
        private const val STARTED = "STARTED"
    }
}