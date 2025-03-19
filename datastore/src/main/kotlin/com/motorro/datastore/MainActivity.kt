package com.motorro.datastore

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.datastore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), WithViewBinding<ActivityMainBinding> by BindingHost() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bindView(ActivityMainBinding::inflate)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        withBinding {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation) as NavHostFragment
            val navController = navHostFragment.navController

            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.vanillaPreferences
                )
            )
            topAppBar.setupWithNavController(navController, appBarConfiguration)
            bottomNavigation.setupWithNavController(navController)

            onBackPressedDispatcher.addCallback(this@MainActivity) {
                if (navController.popBackStack().not()) {
                    finish()
                }
            }
        }
    }
}