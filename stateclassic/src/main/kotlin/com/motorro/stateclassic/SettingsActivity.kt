package com.motorro.stateclassic

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.stateclassic.databinding.ActivitySettingsBinding
import com.motorro.stateclassic.stat.createPageEvent

class SettingsActivity : AppCompatActivity() {
    companion object {
        fun createIntent(context: Context) = android.content.Intent(
            context,
            SettingsActivity::class.java
        )
    }

    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        (application as App).statService.logEvent(createPageEvent("onCreate"))
    }

    override fun onStart() {
        super.onStart()
        (application as App).statService.logEvent(createPageEvent("onStart"))
    }

    override fun onResume() {
        super.onResume()
        (application as App).statService.logEvent(createPageEvent("onResume"))
    }

    override fun onPause() {
        super.onPause()
        (application as App).statService.logEvent(createPageEvent("onPause"))
    }

    override fun onStop() {
        super.onStop()
        (application as App).statService.logEvent(createPageEvent("onStop"))
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as App).statService.logEvent(createPageEvent("onDestroy"))
    }
}