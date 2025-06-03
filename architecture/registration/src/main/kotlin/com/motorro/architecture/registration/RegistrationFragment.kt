package com.motorro.architecture.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.appcore.viewbinding.withBinding
import com.motorro.architecture.registration.databinding.FragmentRegistrationBinding

class RegistrationFragment : Fragment(), WithViewBinding<FragmentRegistrationBinding> by BindingHost() {

    private lateinit var backPressCallback: OnBackPressedCallback

    private val navController: NavController get() {
        val navHostFragment = childFragmentManager.findFragmentById(R.id.registration_navigation) as NavHostFragment
        return navHostFragment.navController
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentRegistrationBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        backPressCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (navController.popBackStack().not()) {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this.viewLifecycleOwner, backPressCallback)

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.nameFragment)
        )

        withBinding {
            topAppBar.setupWithNavController(navController, appBarConfiguration)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backPressCallback.remove()
    }
}