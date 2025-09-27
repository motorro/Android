package com.motorro.cookbook.recipelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.fragment.findNavController
import com.motorro.cookbook.appcore.compose.ui.theme.CookbookTheme
import com.motorro.cookbook.appcore.navigation.Links
import com.motorro.cookbook.appcore.viewbinding.BindingHost
import com.motorro.cookbook.appcore.viewbinding.WithViewBinding
import com.motorro.cookbook.appcore.viewbinding.bindView
import com.motorro.cookbook.appcore.viewbinding.withBinding
import com.motorro.cookbook.recipelist.databinding.FragmentRecipeListBinding
import com.motorro.cookbook.recipelist.ui.RecipeListScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RecipeListFragment : Fragment(), WithViewBinding<FragmentRecipeListBinding> by BindingHost() {

    /**
     * Application deep-links
     */
    @set:Inject
    lateinit var links: Links

    // Factory managed by Hilt
    private val model: RecipeListViewModel by viewModels()

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

        withBinding {
            composeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    CookbookTheme {
                        RecipeListScreen(
                            viewState = model.viewState.collectAsStateWithLifecycle().value,
                            onGesture = model::process,
                            onTerminated = {
                                Log.d(TAG, "Terminated")
                                findNavController().popBackStack()
                            },
                            onRecipe = {
                                Log.d(TAG, "Recipe clicked: $it")
                                findNavController().navigate(links.recipe(it))
                            },
                            onAddRecipe = {
                                Log.d(TAG, "Add recipe clicked")
                                findNavController().navigate(links.addRecipe())
                            },
                            onLogin = {
                                Log.d(TAG, "Login request")
                                findNavController().navigate(links.login())
                            }
                        )
                    }
                }
            }
        }
    }

    companion object {
        const val TAG = "CookbookFragment"
    }
}