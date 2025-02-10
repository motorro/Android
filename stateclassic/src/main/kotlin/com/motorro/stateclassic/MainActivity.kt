package com.motorro.stateclassic

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.motorro.stateclassic.databinding.ActivityMainBinding
import com.motorro.stateclassic.permissions.PermissionHelper
import com.motorro.stateclassic.permissions.goToSettings
import com.motorro.stateclassic.stat.PageEventHelper

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeMenu()
        binding.btnSettings.setOnClickListener { goToSettings() }

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
                    }
                }
            )
        )
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

    private fun showCamera() = with(binding) {
        content.isVisible = true
        permissionRequest.isVisible = false
    }
}