package com.motorro.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.fragments.R
import com.motorro.fragments.databinding.FragmentOrderContentBinding
import com.motorro.fragments.utils.BindingHost
import com.motorro.fragments.utils.WithViewBinding
import com.motorro.fragments.utils.bindView
import com.motorro.fragments.utils.setTitle
import com.motorro.fragments.utils.withBinding

class OrderContentFragment : Fragment(), WithViewBinding<FragmentOrderContentBinding> by BindingHost() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentOrderContentBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            btnCheckout.setOnClickListener {
                (parentFragment as OrderCallback).navigateToCheckout()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.title_your_order)
    }
}