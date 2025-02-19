package com.motorro.fragments.checkout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.fragments.R
import com.motorro.fragments.databinding.FragmentCheckoutBinding
import com.motorro.fragments.utils.BindingHost
import com.motorro.fragments.utils.WithViewBinding
import com.motorro.fragments.utils.bindView
import com.motorro.fragments.utils.setTitle
import com.motorro.fragments.utils.withBinding

class CheckoutFragment : Fragment(), WithViewBinding<FragmentCheckoutBinding> by BindingHost() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentCheckoutBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            topAppBar.setNavigationOnClickListener {
                parentFragmentManager.popBackStack()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.title_checkout)
    }
}