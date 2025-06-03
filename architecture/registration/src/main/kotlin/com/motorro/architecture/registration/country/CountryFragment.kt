package com.motorro.architecture.registration.country

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesFragmentContainer
import com.motorro.architecture.appcore.di.containerModel
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.appcore.viewbinding.withBinding
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.registration.country.di.buildCountryFragmentContainer
import com.motorro.architecture.registration.databinding.FragmentCountryBinding
import kotlinx.coroutines.launch

/**
 * Country entry
 */
internal class CountryFragment : Fragment(), ProvidesFragmentContainer, WithViewBinding<FragmentCountryBinding> by BindingHost(), Logging {

    override lateinit var fragmentContainer: FragmentContainer

    private val viewModel: CountryViewModel by containerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentContainer = buildCountryFragmentContainer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindView(container, FragmentCountryBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            viewLifecycleOwner.lifecycleScope.launch {
                launch {
                    viewModel.countryInput.collect {
                        inputCountry.setTextKeepState(it)
                    }
                }
                launch {
                    viewModel.countrySelect.collect {
                        inputCountry.setSimpleItems(it.toTypedArray())
                    }
                }
                launch {
                    viewModel.countryIsValid.collect {
                        btnNext.isEnabled = it
                    }
                }
            }

            inputCountry.doAfterTextChanged {
                viewModel.setCountryName(it.toString())
            }

            btnNext.setOnClickListener {
                d { "Country confirmed. Navigating to next fragment..." }
                findNavController().navigate(CountryFragmentDirections.countryToNextFragment())
            }
        }
    }
}