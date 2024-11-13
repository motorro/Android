package com.motorro.activitynav

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.activitynav.databinding.ActivityMainBinding

open class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

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


        binding.name.text = activityId.id
        binding.launchStandard.setOnClickListener {
            startActivity(StandardActivity.createIntent(this))
        }
        binding.launchSingleTop.setOnClickListener {
            startActivity(SingleTopActivity.createIntent(this))
        }
        binding.launchSingleTask.setOnClickListener {
            startActivity(SingleTaskActivity.createIntent(this))
        }
        binding.launchSingleInstance.setOnClickListener {
            startActivity(SingleInstanceActivity.createIntent(this))
        }
        binding.launchAnotherTask.setOnClickListener {
            startActivity(AnotherTaskActivity.createIntent(this))
        }
    }
}