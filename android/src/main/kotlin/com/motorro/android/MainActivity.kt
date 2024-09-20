package com.motorro.android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var img: ImageView
    private lateinit var txt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find and save elements in bound view
        img = findViewById(R.id.imageView)
        txt = findViewById(R.id.textView)

        // Find buttons and bind click listeners
        findViewById<Button>(R.id.set_ok).setOnClickListener {
            img.setImageResource(R.drawable.ic_ok)
            txt.text = getString(R.string.looks_good)
        }
        findViewById<Button>(R.id.set_error).setOnClickListener {
            img.setImageResource(R.drawable.ic_error)
            txt.text = getString(R.string.looks_bad)
        }

        // Bind share button action
        findViewById<MaterialToolbar>(R.id.topAppBar).setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share -> {
                    onShare()
                    true
                }
                else -> false
            }
        }
    }

    private fun onShare() {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "text/*"
            putExtra(Intent.EXTRA_TEXT, txt.text)
        }
        startActivity(Intent.createChooser(intent, getString(R.string.share_status)))
    }
}