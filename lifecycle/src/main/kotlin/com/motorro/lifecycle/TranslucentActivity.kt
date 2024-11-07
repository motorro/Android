package com.motorro.lifecycle

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class TranslucentActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: android.content.Context) = Intent(context, TranslucentActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translucent)

        findViewById<Button>(R.id.startTranslucent).setOnClickListener {
            finish()
        }
    }
}