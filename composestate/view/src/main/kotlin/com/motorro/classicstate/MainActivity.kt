package com.motorro.classicstate

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.motorro.classicstate.databinding.ActivityMainBinding
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), WithViewBinding<ActivityMainBinding> by BindingHost() {

    private val count = MutableStateFlow(0)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bindView(ActivityMainBinding::inflate)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initView()
    }

    fun initView() = withBinding {
        lifecycleScope.launch {
            count.collect {
                Log.i("Counter", "Count: $it")
                lblCount.text = getString(R.string.lbl_count, it)
            }
        }

        btnDec.setOnClickListener {
            count.update { (it - 1).coerceAtLeast(0) }
        }

        btnInc.setOnClickListener {
            count.update { it + 1 }
        }
    }
}