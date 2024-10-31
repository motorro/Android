package com.motorro.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.activity.data.mailList
import com.motorro.activity.databinding.ActivityMainBinding
import com.motorro.activity.ui.EmailAdapter
import com.motorro.activity.ui.MarginDecorator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter = EmailAdapter { id ->
        startActivity(MailActivity.getStartIntent(this, id))
    }

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

        setupRecycler()
        binding.add.setOnClickListener {
            startActivity(NewMailActivity.getStartIntent(this))
        }
    }

    /**
     * Sets up recycler view
     */
    private fun setupRecycler() = with(binding) {
        list.adapter = adapter
        list.addItemDecoration(MarginDecorator(resources.getDimensionPixelSize(R.dimen.margin)))
        adapter.submitList(mailList)
    }
}