package com.motorro.fragments.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import com.motorro.fragments.OrderViewModel
import com.motorro.fragments.R
import com.motorro.fragments.databinding.FragmentOrderContentBinding
import com.motorro.fragments.utils.BindingHost
import com.motorro.fragments.utils.WithViewBinding
import com.motorro.fragments.utils.bindView
import com.motorro.fragments.utils.setTitle
import com.motorro.fragments.utils.withBinding

class OrderContentFragment : Fragment(), WithViewBinding<FragmentOrderContentBinding> by BindingHost() {

    // Common view model taken from parent activity
    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentOrderContentBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            val itemAdapter = OrderItemAdapter {
                orderViewModel.remove(it)
            }
            val footerAdapter = OrderFooterAdapter()

            itemList.adapter = ConcatAdapter(
                OrderHeaderAdapter(),
                itemAdapter,
                footerAdapter
            )

            orderViewModel.contents.observe(viewLifecycleOwner) {
                itemAdapter.submitList(it.items)
                footerAdapter.setTotal(it.total)
            }

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