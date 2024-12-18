package com.motorro.view2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.view2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var likes = 0

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

        initLikes()
    }

    private fun initLikes() = with(binding) {
        updateLikes()
        likeButton.setOnClickListener {
            ++likes
            updateLikes()
        }
        dislikeButton.setOnClickListener {
            if (likes > 0) {
                --likes
            }
            updateLikes()
        }
    }

    private fun updateLikes() = with(binding) {
        numLikes.text = getString(R.string.likes, likes)
    }
}