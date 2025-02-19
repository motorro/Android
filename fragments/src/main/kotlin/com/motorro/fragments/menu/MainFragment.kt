package com.motorro.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.motorro.fragments.checkout.CheckoutFragment
import com.motorro.fragments.databinding.FragmentMainBinding
import com.motorro.fragments.utils.BindingHost
import com.motorro.fragments.utils.WithViewBinding
import com.motorro.fragments.utils.bindView
import com.motorro.fragments.utils.containerId
import com.motorro.fragments.utils.withBinding


class MainFragment : Fragment(), WithViewBinding<FragmentMainBinding> by BindingHost() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentMainBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            btnOrder.setOnClickListener {
                parentFragmentManager.commit {
                    replace(containerId, CheckoutFragment())
                    addToBackStack(null)
                }
            }
        }
    }
}