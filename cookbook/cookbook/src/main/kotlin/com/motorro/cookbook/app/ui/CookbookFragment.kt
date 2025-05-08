package com.motorro.cookbook.app.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.motorro.cookbook.app.R
import com.motorro.cookbook.app.data.CookbookError
import com.motorro.cookbook.app.data.RecipeListItem
import com.motorro.cookbook.app.databinding.FragmentCookbookBinding
import com.motorro.core.lce.LceState
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.isVisible
import com.motorro.core.viewbinding.withBinding
import kotlinx.coroutines.launch

class CookbookFragment : Fragment(), WithViewBinding<FragmentCookbookBinding> by BindingHost() {

    private val model by viewModels<CookbookViewModel> {
        CookbookViewModel.Factory(requireContext())
    }

    private val recipeAdapter = RecipeAdapter { id ->
        Log.d(TAG, "Recipe clicked: $id")
        findNavController().navigate(CookbookFragmentDirections.cookbookToRecipe(id.toString()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(
        container,
        FragmentCookbookBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        setupRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.recipes.collect { state -> withBinding {
                    when (state) {
                        is LceState.Content -> {
                            progress.hide()
                            error.isVisible = false
                            auth.isVisible = false
                            recipes.isVisible = true
                            plus.show()
                            enableMenu()
                            onRecipeListUpdated(state.data)
                        }
                        is LceState.Error -> {
                            progress.hide()
                            when (state.error) {
                                is CookbookError.Unauthorized -> {
                                    error.isVisible = false
                                    auth.isVisible = true
                                    recipes.isVisible = false
                                    plus.hide()
                                    disableMenu()
                                }
                                is CookbookError.Unknown -> {
                                    val data = state.data
                                    if (null != data) {
                                        error.isVisible = false
                                        auth.isVisible = false
                                        recipes.isVisible = true
                                        plus.show()
                                        enableMenu()
                                        onRecipeListUpdated(data)
                                    } else {
                                        error.isVisible = true
                                        auth.isVisible = false
                                        recipes.isVisible = false
                                        plus.hide()
                                        disableMenu()
                                        error.textError.text = state.error.message
                                    }
                                }
                            }
                        }
                        is LceState.Loading -> {
                            Log.d(TAG, "Loading recipes")
                            progress.show()
                            error.isVisible = false
                            auth.isVisible = false
                            recipes.isVisible = true
                            plus.show()
                            val data = state.data
                            if (null != data) {
                                plus.show()
                                enableMenu()
                                onRecipeListUpdated(data)
                            } else {
                                plus.hide()
                                disableMenu()
                            }
                        }
                    }
                }}
            }
        }

        withBinding {
            auth.btnLogin.setOnClickListener {
                findNavController().navigate(CookbookFragmentDirections.cookbookToLogin())
            }
            error.btnRetry.setOnClickListener {
                model.refresh()
            }
            plus.setOnClickListener {
                findNavController().navigate(CookbookFragmentDirections.cookbookToAdd())
            }
        }
    }

    private fun setupAppBar() = withBinding {
        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_sync -> {
                    model.refresh()
                    true
                }
                R.id.action_logout -> {
                    model.logout()
                    true
                }
                else -> false
            }
        }
    }

    private fun enableMenu() = withBinding {
        topAppBar.menu.forEach {
            it.isEnabled = true
        }
    }

    private fun disableMenu() = withBinding {
        topAppBar.menu.forEach { it.isEnabled = false }
    }

    private fun setupRecyclerView() = withBinding {
        recipes.adapter = recipeAdapter
        recipes.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.vertical_margin_small)))
    }

    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) {
        recipeAdapter.submitList(recipeList)
    }

    companion object {
        const val TAG = "CookbookFragment"
    }
}