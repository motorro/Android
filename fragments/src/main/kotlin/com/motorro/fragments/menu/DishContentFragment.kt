package com.motorro.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.motorro.fragments.R
import com.motorro.fragments.databinding.FragmentDishContentBinding
import com.motorro.fragments.menu.dish.DishAdapter
import com.motorro.fragments.menu.dish.DishContentViewModel
import com.motorro.fragments.utils.BindingHost
import com.motorro.fragments.utils.MarginDecorator
import com.motorro.fragments.utils.WithViewBinding
import com.motorro.fragments.utils.bindView
import com.motorro.fragments.utils.setTitle
import com.motorro.fragments.utils.withBinding

class DishContentFragment : Fragment(), WithViewBinding<FragmentDishContentBinding> by BindingHost() {

    private val model: DishContentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentDishContentBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = DishAdapter {
            (parentFragment as DishContentCallback).onCategorySelected(it)
        }

        withBinding {
            categoryList.adapter = adapter
            categoryList.addItemDecoration(MarginDecorator(resources.getDimensionPixelSize(R.dimen.vertical_margin)))
        }

        model.categories.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onStart() {
        super.onStart()
        setTitle(R.string.title_our_dishes)
    }
}