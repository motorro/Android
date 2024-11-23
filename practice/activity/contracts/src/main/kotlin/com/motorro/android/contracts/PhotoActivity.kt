package com.motorro.android.contracts

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Size
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.motorro.android.contracts.data.ImageSettings
import com.motorro.android.contracts.databinding.ActivityPhotoBinding
import java.io.File
import java.io.FileOutputStream
import kotlin.time.Clock

/**
 * Activity to take photo
 */
class PhotoActivity : AppCompatActivity() {
    companion object {

        val Intent.imageSettings: ImageSettings
            get() = TODO("Extract image settings from intent")
    }


    class TakePhotoContract : ActivityResultContract<ImageSettings, Uri?>() {
        override fun createIntent(context: Context, input: ImageSettings): Intent {
            TODO("Create intent to take photo with settings")
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            // TODO: Parse result and return photo URI
            return null
        }
    }

    private lateinit var binding: ActivityPhotoBinding

    private lateinit var permissionContract: ActivityResultLauncher<String>
    private fun createPermissionContract() {
        //TODO: Register permission contract launcher
    }

    private fun launchPermissionRequest() {
        //TODO: Launch permission request
    }

    private fun gotoSettings() {
        startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        })
    }

    private fun checkCameraPermission() {
        // TODO: Check camera permission and implement workflow logic
    }

    private fun onCancel() {
        //TODO: Cancel photo taking, pass negative result, finish
        finish()
    }

    private fun onPhotoTaken(fileUri: Uri) {
        //TODO: Pass photo URI as result and finish
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            onCancel()
        }
        binding.toolbar.setTitle(getString(R.string.title_activity_photo, intent.imageSettings.maxSize))

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onCancel()
            }
        })

        createPermissionContract()
    }

    override fun onStart() {
        super.onStart()
        checkCameraPermission()
    }

    private fun showRationale() = with(binding) {
        photoPlaceholder.visibility = GONE
        photoView.visibility = GONE
        rationaleView.visibility = VISIBLE
        settingsView.visibility = GONE

        btnPermission.setOnClickListener {
            launchPermissionRequest()
        }
    }

    private fun showSettings() = with(binding) {
        photoPlaceholder.visibility = GONE
        photoView.visibility = GONE
        rationaleView.visibility = GONE
        settingsView.visibility = VISIBLE

        btnSettings.setOnClickListener {
            gotoSettings()
        }
    }

    private fun showCamera() = with(binding) {
        photoPlaceholder.visibility = GONE
        photoView.visibility = VISIBLE
        rationaleView.visibility = GONE
        settingsView.visibility = GONE

        val (maxSide, quality) = intent.imageSettings

        val cameraController = LifecycleCameraController(this@PhotoActivity).apply {
            bindToLifecycle(this@PhotoActivity)
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            imageCaptureResolutionSelector = ResolutionSelector.Builder().apply {
                setAspectRatioStrategy(AspectRatioStrategy(AspectRatio.RATIO_4_3, AspectRatioStrategy.FALLBACK_RULE_AUTO))
                setResolutionStrategy(
                    ResolutionStrategy(
                        Size(maxSide, maxSide * 3 / 4),
                        ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
                    )
                )
            }.build()
        }
        preview.controller = cameraController

        shutter.setOnClickListener {
            cameraController.takePicture(ContextCompat.getMainExecutor(this@PhotoActivity), object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    image.use { img ->
                        onPhotoTaken(img.prepareBitmap(maxSide).saveImage(quality))
                    }
                }
            })
        }
    }

    private fun ImageProxy.prepareBitmap(maxSide: Int): Bitmap {
        fun rotate(source: Bitmap, angle: Float, s: Float): Bitmap {
            val matrix = Matrix().apply {
                postRotate(angle)
                postScale(s, s)
            }
            return Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, true)
        }

        val bitmap = toBitmap()
        val rotation = imageInfo.rotationDegrees
        val maxSize = Integer.max(bitmap.width, bitmap.height)
        val scale = maxSide.toFloat() / maxSize

        return rotate(bitmap, rotation.toFloat(), scale)
    }

    private fun Bitmap.saveImage(quality: Int): Uri {
        val file = File(applicationContext.cacheDir, "photo-${Clock.System.now().toEpochMilliseconds()}.jpg")
        FileOutputStream(file).use {
            compress(Bitmap.CompressFormat.PNG, quality, it)
            it.flush()
        }
        return FileProvider.getUriForFile(applicationContext, applicationContext.packageName, file)
    }
}