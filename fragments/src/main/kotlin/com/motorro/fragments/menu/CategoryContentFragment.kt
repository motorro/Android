package com.motorro.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.motorro.fragments.R
import com.motorro.fragments.data.CategoryId
import com.motorro.fragments.data.ItemId
import com.motorro.fragments.databinding.FragmentCategoryContentBinding
import com.motorro.fragments.menu.category.CategoryContentViewModel
import com.motorro.fragments.menu.category.ItemAdapter
import com.motorro.fragments.menu.category.ItemCallback
import com.motorro.fragments.utils.BindingHost
import com.motorro.fragments.utils.MarginDecorator
import com.motorro.fragments.utils.WithViewBinding
import com.motorro.fragments.utils.bindView
import com.motorro.fragments.utils.setTitle
import com.motorro.fragments.utils.withBinding

class CategoryContentFragment : Fragment(), WithViewBinding<FragmentCategoryContentBinding> by BindingHost() {

    companion object {
        const val RESULT = "category_result"
        private const val EXTRA_CATEGORY = "extra_category"

        fun newInstance(categoryId: CategoryId): CategoryContentFragment = CategoryContentFragment().apply {
            arguments = Bundle().apply {
                putInt(EXTRA_CATEGORY, categoryId.id)
            }
        }
    }

    private val viewModel: CategoryContentViewModel by viewModels {
        CategoryContentViewModel.Factory(CategoryId(requireArguments().getInt(EXTRA_CATEGORY)))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentCategoryContentBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            itemList.adapter = ItemAdapter(object : ItemCallback {
                override fun onAdd(itemId: ItemId) {
                    viewModel.add(itemId)
                }
                override fun onRemove(itemId: ItemId) {
                    viewModel.remove(itemId)
                }
            })
            itemList.addItemDecoration(MarginDecorator(resources.getDimensionPixelSize(R.dimen.vertical_margin)))

            addToOrder.setOnClickListener {
                setFragmentResult(RESULT, Bundle().apply {
                    putParcelable(RESULT, viewModel.getResult())
                })
            }
        }

        viewModel.state.observe(viewLifecycleOwner) {
            withBinding {
                title.text = it.name
                setTitle(it.name)

                (itemList.adapter as ItemAdapter).submitList(it.items)

                addToOrder.isEnabled = it.addEnabled
            }
        }
    }
}