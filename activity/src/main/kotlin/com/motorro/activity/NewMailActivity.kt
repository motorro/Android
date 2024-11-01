package com.motorro.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.activity.databinding.ActivityNewMailBinding

/**
 * Mail activity
 */
class NewMailActivity : AppCompatActivity() {
    companion object {
        /**
         * Gets intent to start mail activity
         */
        fun getStartIntent(context: Context) = Intent(context, NewMailActivity::class.java)

        // The mail URL is not always passed as hierarchical data, so we use a regex to extract it
        private val EMAIL = "[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}".toRegex()
        private val SUBJECT = "subject=([^&]+)".toRegex()
        private val BODY = "body=([^&]+)".toRegex()

        private fun Intent.address(): String =
            getStringExtra(Intent.EXTRA_EMAIL)
                ?: data?.toString()?.let(EMAIL::find)?.value
                ?: ""

        private fun Intent.subject(): String =
            getStringExtra(Intent.EXTRA_SUBJECT)
                ?: data?.query?.let(SUBJECT::find)?.groups?.get(1)?.value
                ?: ""

        private fun Intent.text(): String =
            getStringExtra(Intent.EXTRA_TEXT)
                ?: data?.query?.let(BODY::find)?.groups?.get(1)?.value
                ?: ""
    }

    private lateinit var binding: ActivityNewMailBinding

    private val takePicture: ActivityResultLauncher<Void?> = registerForActivityResult(

        // Contract to take a picture
        ActivityResultContracts.TakePicturePreview(),

        // Callback to handle the result
        { photo ->
            // Handle photo
            with(binding.attachment) {
                setImageBitmap(photo)
                visibility = if (photo != null) {
                    android.view.View.VISIBLE
                } else {
                    android.view.View.GONE
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityNewMailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.topAppBar.setNavigationOnClickListener {
            finish()
        }
        binding.send.setOnClickListener {
            // Send mail
            finish()
        }
        binding.photo.setOnClickListener {
            // Take photo
            takePicture.launch(null)
        }

        loadLetter()
    }

    private fun loadLetter() = with(binding){
        to.setText(intent.address())
        subject.setText(intent.subject())
        this.message.setText(intent.text())
    }
}