package com.motorro.stateclassic

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.motorro.stateclassic.databinding.ActivityMainBinding
import com.motorro.stateclassic.permissions.PermissionHelper
import com.motorro.stateclassic.permissions.goToSettings
import com.motorro.stateclassic.stat.PageEventHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var capturedBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: MainViewModel by viewModels {
        MainViewModel.Factory((application as App).preferences)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeMenu()
        initializeContent()
        initializeView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycle.addObserver(PageEventHelper((application as App).statService))
        lifecycle.addObserver(
            PermissionHelper(
                this,
                activityResultRegistry,
                setOf(android.Manifest.permission.CAMERA),
                onGranted = { showCamera() },
                onDenied = {
                    with(binding) {
                        content.isVisible = false
                        permissionRequest.isVisible = true
                        capturedBehavior.isHideable = true
                        capturedBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                        plus.hide()
                    }
                }
            )
        )
    }

    private fun initializeView() = with(binding) {
        btnSettings.setOnClickListener { goToSettings() }
        plus.setOnClickListener {
            viewModel.addDetectedText()
        }
        share.setOnClickListener {
            startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, viewModel.content.value)
            }, null))
        }
        viewModel.content.observe(this@MainActivity) {
            text.text = it
        }
        viewModel.shareEnabled.observe(this@MainActivity) {
            share.isEnabled = it
        }
        viewModel.addEnabled.observe(this@MainActivity) {
            plus.isEnabled = it
        }
        viewModel.detectedRects.observe(this@MainActivity) {
            overlay.rectBounds = it
        }
    }

    private fun initializeMenu() = with(binding) {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_settings -> {
                    startActivity(SettingsActivity.createIntent(this@MainActivity))
                    true
                }
                else -> false
            }
        }
    }

    private fun initializeContent() {
        capturedBehavior = BottomSheetBehavior.from(binding.captured)
        capturedBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do nothing
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        binding.plus.hide()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        binding.plus.show()
                    }
                }
            }
        })
    }

    private fun showCamera() {
        with(binding) {
            content.isVisible = true
            permissionRequest.isVisible = false
            capturedBehavior.isHideable = false
            capturedBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            plus.show()
        }
        val executor = ContextCompat.getMainExecutor(this)
        val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        binding.viewFinder.controller = LifecycleCameraController(this).apply {
            setImageAnalysisAnalyzer(
                executor,
                MlKitAnalyzer(
                    listOf(textRecognizer),
                    COORDINATE_SYSTEM_VIEW_REFERENCED,
                    executor
                ) { analyzerResult ->
                    analyzerResult.getValue(textRecognizer)?.let { result: Text ->
                        viewModel.updateRecognizedText(result)
                    }
                }
            )
            bindToLifecycle(this@MainActivity)
        }
    }
}