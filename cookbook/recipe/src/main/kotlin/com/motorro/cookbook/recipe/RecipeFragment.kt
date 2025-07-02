package com.motorro.cookbook.recipe

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
import com.bumptech.glide.Glide
import com.motorro.cookbook.appcore.di.DiContainer
import com.motorro.cookbook.appcore.navigation.Links
import com.motorro.cookbook.appcore.utils.subscribeToResult
import com.motorro.cookbook.appcore.viewbinding.BindingHost
import com.motorro.cookbook.appcore.viewbinding.WithViewBinding
import com.motorro.cookbook.appcore.viewbinding.bindView
import com.motorro.cookbook.appcore.viewbinding.isVisible
import com.motorro.cookbook.appcore.viewbinding.withBinding
import com.motorro.cookbook.core.error.UnknownException
import com.motorro.cookbook.core.lce.LceState
import com.motorro.cookbook.domain.session.error.UnauthorizedException
import com.motorro.cookbook.model.Recipe
import com.motorro.cookbook.recipe.DeleteConfirmationFragment.Companion.CONFIRMATION_RESULT
import com.motorro.cookbook.recipe.R
import com.motorro.cookbook.recipe.databinding.FragmentRecipeBinding
import kotlinx.coroutines.launch
import kotlin.uuid.Uuid
import com.motorro.cookbook.appcore.R as CR

class RecipeFragment : Fragment(), WithViewBinding<FragmentRecipeBinding> by BindingHost() {

    /**
     * Application deep-links
     */
    private lateinit var links: Links

    private val recipeId: Uuid get() = Uuid.parse(RecipeFragmentArgs.fromBundle(requireArguments()).recipeId)
    private val model by viewModels<RecipeViewModel> {
        RecipeViewModel.Factory(requireContext(), recipeId)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        links = (requireActivity().application as DiContainer).links
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentRecipeBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        setupAlertResult()
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.recipe.collect { recipeLce -> withBinding {
                    setTitle(recipeLce.data?.title)
                    when (recipeLce) {
                        is LceState.Loading -> {
                            Log.d(TAG, "Loading recipe $recipeId")
                            error.isVisible = false
                            auth.isVisible = false
                            isVisible = true
                            progress.show()
                            val data = recipeLce.data
                            if (null != data) {
                                enableMenu()
                                displayRecipe(data)
                            } else {
                                disableMenu()
                            }
                        }
                        is LceState.Error -> {
                            Log.e(TAG, "Error loading recipe $recipeId", recipeLce.error)
                            progress.hide()
                            when(recipeLce.error) {
                                is UnauthorizedException -> {
                                    error.isVisible = false
                                    auth.isVisible = true
                                    content.isVisible = false
                                    disableMenu()
                                }
                                is UnknownException -> {
                                    val data = recipeLce.data
                                    if (null != data) {
                                        error.isVisible = false
                                        auth.isVisible = false
                                        content.isVisible = true
                                        enableMenu()
                                        displayRecipe(data)
                                    } else {
                                        error.isVisible = true
                                        auth.isVisible = false
                                        content.isVisible = false
                                        disableMenu()
                                        error.textError.text = recipeLce.error.message
                                    }
                                }
                            }
                        }
                        is LceState.Content -> {
                            Log.e(TAG, "Recipe $recipeId loaded")
                            progress.hide()
                            error.isVisible = false
                            auth.isVisible = false
                            content.isVisible = true
                            enableMenu()
                            displayRecipe(recipeLce.data)
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
        }
    }

    private fun setupAppBar() = withBinding {
        topAppBar.setNavigationOnClickListener {
            close()
        }
        topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_delete -> {
                    deleteRecipe()
                    true
                }
                else -> false
            }
        }
    }

    private fun setTitle(value: String?) = withBinding {
        topAppBar.title = value ?: getString(R.string.title_recipe)
    }

    private fun enableMenu() = withBinding {
        topAppBar.menu.forEach {
            it.isEnabled = true
        }
    }

    private fun disableMenu() = withBinding {
        topAppBar.menu.forEach { it.isEnabled = false }
    }

    private fun setupAlertResult() = subscribeToResult(CONFIRMATION_RESULT) { result: Boolean ->
        if (result) {
            Log.d(TAG, "Deleting recipe $recipeId")
            model.deleteRecipe()
            close()
        }
    }

    private fun close() {
        findNavController().popBackStack()
    }

    private fun displayRecipe(recipe: Recipe) = withBinding {
        title.text = recipe.title
        val source = recipe.image?.url ?: CR.drawable.ic_no_image
        Glide.with(this.root).load(source)
            .fallback(CR.drawable.ic_no_image)
            .placeholder(CR.drawable.ic_no_image)
            .centerCrop()
            .into(image)
        category.text = recipe.category.name
        description.text = recipe.description
    }

    private fun deleteRecipe() {
        Log.d(TAG, "Deleting recipe $recipeId")
        findNavController().navigate(
            RecipeFragmentDirections.recipeToDeleteConfirmation(
                model.recipe.value.data?.title.orEmpty()
            )
        )
    }

    companion object {
        private const val TAG = "RecipeFragment"
    }
}