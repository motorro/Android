package com.motorro.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.activity.data.mailList
import com.motorro.activity.databinding.ActivityMailBinding
import com.motorro.activity.ui.formatLocal

/**
 * Mail activity
 */
class MailActivity : AppCompatActivity() {
    companion object {
        /**
         * Gets intent to start mail activity
         */
        fun getStartIntent(context: Context, letterId: Int) = Intent(context, MailActivity::class.java).apply {
            putExtra("letterId", letterId)
        }

        /**
         * Gets letter ID from intent passed to launch activity
         */
        private val Intent.letterId: Int
            get() = requireNotNull(getIntExtra("letterId", -1).takeIf { it >= 0 }) {
                "Letter ID should be supplied"
            }
    }

    private lateinit var binding: ActivityMailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }

        loadLetter()
    }

    private fun loadLetter() = with(binding){
        val letterId = intent.letterId
        val letter = mailList.find { it.id == letterId } ?: run {
            finish()
            return
        }

        subject.text = letter.subject
        address.text = letter.address
        date.text = letter.created.formatLocal()
        body.text = letter.body
    }
}