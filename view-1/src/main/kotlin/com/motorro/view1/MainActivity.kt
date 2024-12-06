package com.motorro.view1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.view1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupChange()
        setupNavigation()
    }

    private fun setupChange()  {
        binding.change.setOnClickListener {
            binding.text.text = getString(R.string.changed_text)
        }
    }

    private fun setupNavigation() {
        binding.toFrame.setOnClickListener {
            startActivity(Intent(this, FrameActivity::class.java))
        }
        binding.toLinear.setOnClickListener {
            startActivity(Intent(this, LinearActivity::class.java))
        }
        binding.toNested.setOnClickListener {
            startActivity(Intent(this, NestedActivity::class.java))
        }
    }
}