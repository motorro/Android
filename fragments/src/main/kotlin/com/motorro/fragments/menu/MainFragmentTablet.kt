package com.motorro.fragments.menu

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.motorro.fragments.R
import com.motorro.fragments.checkout.CheckoutFragment
import com.motorro.fragments.data.CategoryId
import com.motorro.fragments.databinding.FragmentMainBinding
import com.motorro.fragments.menu.category.CategoryResult
import com.motorro.fragments.order.OrderCallback
import com.motorro.fragments.order.OrderContentFragment
import com.motorro.fragments.utils.BindingHost
import com.motorro.fragments.utils.WithViewBinding
import com.motorro.fragments.utils.bindView
import com.motorro.fragments.utils.containerId


class MainFragmentTablet : Fragment(), WithViewBinding<FragmentMainBinding> by BindingHost(), OrderCallback, DishContentCallback {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentMainBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        @Suppress("DEPRECATION")
        childFragmentManager.setFragmentResultListener(CategoryContentFragment.RESULT, viewLifecycleOwner) { _, result ->
            val resultData: CategoryResult? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.getParcelable(CategoryContentFragment.RESULT, CategoryResult::class.java)
            } else {
                result.getParcelable(CategoryContentFragment.RESULT)
            }
            navigateToOrder()
        }
    }

    private fun navigateToOrder() {
        childFragmentManager.commit {
            replace(R.id.content, OrderContentFragment())
        }
    }

    override fun navigateToCheckout() {
        parentFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
            )
            replace(containerId, CheckoutFragment())
            addToBackStack(null)
        }
    }

    override fun onCategorySelected(categoryId: CategoryId) {
        childFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
            )
            replace(R.id.content, CategoryContentFragment.newInstance(categoryId))
        }
    }
}