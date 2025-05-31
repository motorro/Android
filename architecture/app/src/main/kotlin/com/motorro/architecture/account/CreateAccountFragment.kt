package com.motorro.architecture.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : Fragment(), WithViewBinding<FragmentCreateAccountBinding> by BindingHost() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentCreateAccountBinding::inflate)
    }
}