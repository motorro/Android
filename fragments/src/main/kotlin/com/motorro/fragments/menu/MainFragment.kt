package com.motorro.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.motorro.fragments.R
import com.motorro.fragments.checkout.CheckoutFragment
import com.motorro.fragments.data.CategoryId
import com.motorro.fragments.databinding.FragmentMainBinding
import com.motorro.fragments.order.OrderCallback
import com.motorro.fragments.order.OrderContentFragment
import com.motorro.fragments.utils.BindingHost
import com.motorro.fragments.utils.WithTitle
import com.motorro.fragments.utils.WithViewBinding
import com.motorro.fragments.utils.bindView
import com.motorro.fragments.utils.containerId
import com.motorro.fragments.utils.withBinding


class MainFragment : Fragment(), WithViewBinding<FragmentMainBinding> by BindingHost(), WithTitle, OrderCallback, DishContentCallback {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentMainBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateTopBar()
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (0 == childFragmentManager.backStackEntryCount) {
                    isEnabled = false
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                    isEnabled = true
                } else {
                    childFragmentManager.popBackStack()
                }
            }
        })
        withBinding {
            topAppBar.setNavigationOnClickListener {
                childFragmentManager.popBackStack()
            }
        }
        childFragmentManager.addOnBackStackChangedListener {
            updateTopBar()
        }

        withBinding {
            btnOrder?.setOnClickListener {
                toChild{ OrderContentFragment() }
            }
        }
    }

    private fun updateTopBar() {
        val isEmpty = 0 == childFragmentManager.backStackEntryCount
        withBinding {
            if (isEmpty) {
                btnOrder?.show()
                topAppBar.navigationIcon = null
                topAppBar.title = getString(R.string.title_menu)
            } else {
                btnOrder?.hide()
                topAppBar.navigationIcon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_back)
            }
        }
    }

    private inline fun toChild(block: () -> Fragment) {
        withBinding {
            btnOrder?.hide()
            topAppBar.setNavigationIcon(R.drawable.ic_back)
        }

        childFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,  // enter
                R.anim.fade_out,  // exit
                R.anim.fade_in,   // popEnter
                R.anim.slide_out  // popExit
            )
            replace(containerId, block())
            addToBackStack(null)
        }
    }

    override fun setTitle(title: String) = withBinding {
        topAppBar.title = title
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
        TODO("Not yet implemented")
    }
}