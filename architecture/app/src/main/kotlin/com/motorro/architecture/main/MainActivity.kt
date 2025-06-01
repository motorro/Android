package com.motorro.architecture.main

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.motorro.architecture.R
import com.motorro.architecture.appcore.di.ActivityContainer
import com.motorro.architecture.appcore.di.ProvidesActivityContainer
import com.motorro.architecture.appcore.di.containerModel
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.databinding.ActivityMainBinding
import com.motorro.architecture.main.data.MainScreenState
import com.motorro.architecture.main.di.buildMainActivityContainer
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ProvidesActivityContainer, WithViewBinding<ActivityMainBinding> by BindingHost() {
    override lateinit var activityContainer: ActivityContainer

    /**
     * Model created by component
     */
    private val model: MainViewModel by containerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        activityContainer = buildMainActivityContainer()

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

        onBackPressedDispatcher.addCallback(this) {
            if (navController.popBackStack().not()) {
                finish()
            }
        }

        lifecycleScope.launch {
            model.state.collect {
                when(it) {
                    MainScreenState.Authenticating -> navController.navigate(
                        R.id.createAccountFragment,
                        null,
                        navOptions {
                            launchSingleTop = true
                        }
                    )
                    MainScreenState.Content -> navController.navigate(
                        R.id.contentFragment,
                        null,
                        navOptions {
                            launchSingleTop = true
                        }
                    )
                    MainScreenState.Registering -> navController.navigate(
                        R.id.createProfileFragment,
                        null,
                        navOptions {
                            launchSingleTop = true
                        }
                    )
                }
            }
        }
    }
}