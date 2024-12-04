package com.motorro.view1

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.ViewGroup.LayoutParams
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Create layout
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
        }
        val layoutParams = LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        setContentView(layout, layoutParams)

        // Create text view
        val text = TextView(this).apply {
            text = getString(R.string.hello_world)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
            setLayoutParams(LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ))
        }
        layout.addView(text)

        // Create change button
        val changeButton = MaterialButton(this).apply {
            setText(getString(R.string.change))
            setLayoutParams(LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ))
            setOnClickListener {
                text.text = getString(R.string.changed_text)
            }
        }
        layout.addView(changeButton)
    }
}