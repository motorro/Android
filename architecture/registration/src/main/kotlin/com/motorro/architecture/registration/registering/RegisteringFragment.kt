package com.motorro.architecture.registration.registering

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.architecture.appcore.di.FragmentContainer
import com.motorro.architecture.appcore.di.ProvidesFragmentContainer
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.registration.databinding.FragmentRegisteringBinding

internal class RegisteringFragment : Fragment(), ProvidesFragmentContainer, WithViewBinding<FragmentRegisteringBinding> by BindingHost() {

    override lateinit var fragmentContainer: FragmentContainer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentRegisteringBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}