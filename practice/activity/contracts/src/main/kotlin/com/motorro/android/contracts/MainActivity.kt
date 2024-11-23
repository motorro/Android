package com.motorro.android.contracts

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.appcompat.app.AppCompatActivity
import com.motorro.android.contracts.data.ImageSettings
import com.motorro.android.contracts.databinding.ActivityMainBinding
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var sharedImageUri: Uri? by Delegates.observable(null) { _, _, newValue ->
        binding.picture.setImageURI(newValue)
    }


    private lateinit var photoContract: ActivityResultLauncher<ImageSettings>
    private fun createPhotoContract() {
        //TODO: Create take photo contract
    }

    private lateinit var selectContract: ActivityResultLauncher<PickVisualMediaRequest>
    private fun createSelectImageContract() {
        //TODO: Create select image contract
    }

    private fun shareImage() {
        // Проверяем, есть ли что шарить
        val toShare = sharedImageUri ?: return

        // TODO: Create share intent and intent chooser
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createSelectImageContract()
        createPhotoContract()

        binding.toolbar.apply {
            inflateMenu(R.menu.main_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share_item -> {
                        shareImage()
                        true
                    }
                    else -> false
                }
            }
        }

        binding.btnTakePhoto.setOnClickListener {
            TODO("Launch take photo contract")
        }

        binding.btnSelectPicture.setOnClickListener {
            TODO("Launch select picture contract")
        }
    }
}