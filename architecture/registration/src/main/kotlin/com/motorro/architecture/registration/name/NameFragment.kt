package com.motorro.architecture.registration.name

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
import com.motorro.architecture.registration.databinding.FragmentNameBinding
import com.motorro.architecture.registration.name.di.buildNameFragmentContainer
import kotlinx.coroutines.launch

/**
 * Name entry
 */
internal class NameFragment : Fragment(), ProvidesFragmentContainer, WithViewBinding<FragmentNameBinding> by BindingHost(), Logging {

    override lateinit var fragmentContainer: FragmentContainer

    private val viewModel: NameViewModel by containerModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentContainer = buildNameFragmentContainer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentNameBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            viewLifecycleOwner.lifecycleScope.launch {
                launch {
                    viewModel.name.collect {
                        inputName.setTextKeepState(it)
                    }
                }
                launch {
                    viewModel.nameIsValid.collect {
                        btnNext.isEnabled = it
                    }
                }
            }

            inputName.doAfterTextChanged {
                viewModel.setName(it.toString())
            }

            btnNext.setOnClickListener {
                d { "Name confirmed. Navigating to next fragment..." }
                findNavController().navigate(NameFragmentDirections.nameToNextFragment())
            }
        }
    }
}