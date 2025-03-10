package com.motorro.cookbook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.motorro.cookbook.R
import com.motorro.cookbook.data.RecipeCategory
import com.motorro.cookbook.data.RecipeListItem
import com.motorro.cookbook.databinding.FragmentCookbookBinding

class CookbookFragment : Fragment() {

    private val binding = FragmentBindingDelegate<FragmentCookbookBinding>(this)
    private lateinit var capturedBehavior: BottomSheetBehavior<ConstraintLayout>
    private val model by viewModels<CookbookViewModel> {
        CookbookViewModel.Factory(requireContext())
    }

    private val recipeAdapter = RecipeAdapter { id ->
        Log.d(TAG, "Recipe clicked: $id")
        findNavController().navigate(CookbookFragmentDirections.cookbookToRecipe(id))
    }
    private val categoryAdapter = CategoryFilterAdapter {
        Log.d(TAG, "Category toggled: $it")
        model.toggleCategory(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentCookbookBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFilter()

        model.recipes.observe(viewLifecycleOwner, ::onRecipeListUpdated)

        model.query.observe(viewLifecycleOwner, ::onNameFilterListUpdated)
        binding.withBinding {
            search.doOnTextChanged { text, _, _, _ ->
                model.setQuery(text.toString())
            }
        }

        model.categories.observe(viewLifecycleOwner, ::onCategoryFilterListUpdated)

        binding.withBinding {
            plus.setOnClickListener {
                findNavController().navigate(CookbookFragmentDirections.cookbookToAdd())
            }
        }
    }

    private fun setupFilter() = binding.withBinding {
        capturedBehavior = BottomSheetBehavior.from(filter)
        capturedBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                // Do nothing
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        plus.hide()
                    }
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        plus.show()
                    }
                }
            }
        })
    }

    private fun setupRecyclerView() = binding.withBinding {
        recipes.adapter = recipeAdapter
        recipes.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.vertical_margin_small)))
        categories.adapter = categoryAdapter
    }

    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) {
        recipeAdapter.submitList(recipeList)
    }

    private fun onCategoryFilterListUpdated(categoryList: List<Pair<RecipeCategory, Boolean>>) {
        categoryAdapter.submitList(categoryList)
    }

    private fun onNameFilterListUpdated(query: String?) = binding.withBinding {
        search.setTextKeepState(query.orEmpty())
    }

    companion object {
        const val TAG = "CookbookFragment"
    }
}