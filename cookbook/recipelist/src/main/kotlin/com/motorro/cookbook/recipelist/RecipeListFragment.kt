package com.motorro.cookbook.recipelist

import android.content.Context
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
import com.motorro.cookbook.app.ui.RecipeAdapter
import com.motorro.cookbook.appcore.di.DiContainer
import com.motorro.cookbook.appcore.navigation.Links
import com.motorro.cookbook.appcore.ui.MarginItemDecoration
import com.motorro.cookbook.appcore.viewbinding.BindingHost
import com.motorro.cookbook.appcore.viewbinding.WithViewBinding
import com.motorro.cookbook.appcore.viewbinding.bindView
import com.motorro.cookbook.appcore.viewbinding.isVisible
import com.motorro.cookbook.appcore.viewbinding.withBinding
import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import com.motorro.cookbook.recipelist.data.RecipeListItem
import com.motorro.cookbook.recipelist.databinding.FragmentRecipeListBinding
import kotlinx.coroutines.launch
import com.motorro.cookbook.appcore.R as CR

class RecipeListFragment : Fragment(), WithViewBinding<FragmentRecipeListBinding> by BindingHost() {

    /**
     * Application deep-links
     */
    private lateinit var links: Links

    private val model by viewModels<RecipeListViewModel> {
        RecipeListViewModel.Factory(requireContext())
    }

    private val recipeAdapter = RecipeAdapter { id ->
        Log.d(TAG, "Recipe clicked: $id")
        findNavController().navigate(links.recipe(id))
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        links = (requireActivity().application as DiContainer).links
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(
        container,
        FragmentRecipeListBinding::inflate
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
                                is UnauthorizedException -> {
                                    error.isVisible = false
                                    auth.isVisible = true
                                    recipes.isVisible = false
                                    plus.hide()
                                    disableMenu()
                                }
                                is UnknownException -> {
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
                findNavController().navigate(links.login())
            }
            error.btnRetry.setOnClickListener {
                model.refresh()
            }
            plus.setOnClickListener {
                findNavController().navigate(links.addRecipe())
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
        recipes.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(CR.dimen.vertical_margin_small)))
    }

    private fun onRecipeListUpdated(recipeList: List<RecipeListItem>) {
        recipeAdapter.submitList(recipeList)
    }

    companion object {
        const val TAG = "CookbookFragment"
    }
}