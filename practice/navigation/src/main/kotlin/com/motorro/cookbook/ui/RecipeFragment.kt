package com.motorro.cookbook.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.motorro.cookbook.R
import com.motorro.cookbook.data.Recipe
import com.motorro.cookbook.databinding.FragmentRecipeBinding

class RecipeFragment : Fragment() {

    private val recipeId: Int get() = RecipeFragmentArgs.fromBundle(requireArguments()).recipeId
    private val model by viewModels<RecipeFragmentViewModel> {
        RecipeFragmentViewModel.Factory(requireContext(), recipeId)
    }

    private val binding = FragmentBindingDelegate<FragmentRecipeBinding>(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentRecipeBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAppBar()
        setupAlertResult()
        model.recipe.observe(viewLifecycleOwner, ::displayRecipe)
    }

    private fun setupAppBar() = binding.withBinding {
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

    private fun setupAlertResult() {
        //TODO("Subscribe to alert result")
    }

    private fun close() {
        findNavController().popBackStack()
    }

    private fun displayRecipe(recipe: Recipe) = binding.withBinding {
        title.text = recipe.title
        Glide.with(this.root)
            .load(recipe.imageUrl)
            .placeholder(R.drawable.ic_image)
            .centerCrop()
            .into(image)
        category.text = recipe.category.name
        steps.text = recipe.steps.joinToString("\n")
    }

    private fun deleteRecipe() {
        Log.d(TAG, "Deleting recipe $recipeId")
        TODO("Navigate to delete confirmation")
    }

    companion object {
        private const val TAG = "RecipeFragment"
    }
}