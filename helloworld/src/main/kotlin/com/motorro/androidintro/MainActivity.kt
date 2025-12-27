package com.motorro.androidintro

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.motorro.androidintro.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hello.text = getString(R.string.hello_world)
    }
}