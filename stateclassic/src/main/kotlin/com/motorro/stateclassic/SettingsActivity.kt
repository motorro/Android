package com.motorro.stateclassic

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.motorro.stateclassic.databinding.ActivitySettingsBinding
import com.motorro.stateclassic.stat.PageEventHelper

class SettingsActivity : AppCompatActivity() {
    companion object {
        fun createIntent(context: Context) = android.content.Intent(
            context,
            SettingsActivity::class.java
        )
    }

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels {
        SettingsViewModel.Factory((application as App).preferences)
    }

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

        lifecycle.addObserver(PageEventHelper((application as App).statService))

        viewModel.prefix.observe(this) {
            binding.prefixEditText.setTextKeepState(it.orEmpty())
        }
        binding.prefixEditText.addTextChangedListener {
            viewModel.updatePrefix(it.toString())
        }
    }
}