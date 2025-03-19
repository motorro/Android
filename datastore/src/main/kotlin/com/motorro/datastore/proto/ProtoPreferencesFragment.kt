package com.motorro.datastore.proto

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.datastore.bindViewModel
import com.motorro.datastore.databinding.FragmentPreferencesBinding

class ProtoPreferencesFragment : Fragment(), WithViewBinding<FragmentPreferencesBinding> by BindingHost() {

    private val model by viewModels<ProtoPreferencesViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentPreferencesBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bindViewModel(model)
    }
}