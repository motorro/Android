package com.motorro.di

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
import com.motorro.di.databinding.ActivityMainBinding
import com.motorro.di.di.MainActivityComponent
import com.motorro.di.di.ProvidesApplicationComponent
import com.motorro.di.di.ProvidesMainActivityComponent

class MainActivity : AppCompatActivity(), WithViewBinding<ActivityMainBinding> by BindingHost(), ProvidesMainActivityComponent {

    override lateinit var mainActivityComponent: MainActivityComponent
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        mainActivityComponent = (application as ProvidesApplicationComponent)
            .applicationComponent
            .mainActivityComponentBuilder()
            .build(this)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bindView(ActivityMainBinding::inflate)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navigation) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.mainFragment)
        )

        withBinding {
            setSupportActionBar(topAppBar)
            topAppBar.setupWithNavController(navController, appBarConfiguration)
        }

        onBackPressedDispatcher.addCallback(this) {
            if (navController.popBackStack().not()) {
                finish()
            }
        }
    }
}