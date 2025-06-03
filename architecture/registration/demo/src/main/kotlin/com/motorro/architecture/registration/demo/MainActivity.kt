package com.motorro.architecture.registration.demo

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.registration.demo.databinding.ActivityMainBinding
import com.motorro.architecture.registration.demo.di.buildContainer

class MainActivity : AppCompatActivity(), ProvidesActivityContainer, WithViewBinding<ActivityMainBinding> by BindingHost() {

    override lateinit var activityContainer: ActivityContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        activityContainer = buildContainer()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bindView(ActivityMainBinding::inflate)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}