package com.motorro.filestore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.filestore.databinding.FragmentChooseSourceBinding

class ChooseSourceFragment : Fragment(), WithViewBinding<FragmentChooseSourceBinding> by BindingHost() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentChooseSourceBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            btnShootSmall.setOnClickListener {
                findNavController().navigate(ChooseSourceFragmentDirections.chooseToTakePreview())
            }
        }
    }
}