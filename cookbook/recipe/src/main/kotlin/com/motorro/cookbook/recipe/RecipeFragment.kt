package com.motorro.cookbook.recipe

import android.os.Bundle
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
import com.motorro.cookbook.recipe.databinding.FragmentRecipeBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import javax.inject.Inject
import kotlin.uuid.Uuid

@AndroidEntryPoint
class RecipeFragment : Fragment(), WithViewBinding<FragmentRecipeBinding> by BindingHost() {

    /**
     * Application deep-links
     */
    @set:Inject
    lateinit var links: Links

    private val recipeId: Uuid get() = Uuid.parse(RecipeFragmentArgs.fromBundle(requireArguments()).recipeId)

    private val model by viewModels<RecipeViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<RecipeViewModel.Factory> {
                it.create(recipeId)
            }
        }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(container, FragmentRecipeBinding::inflate)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            composeView.apply {
                setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
                setContent {
                    CookbookTheme {
                        RecipeScreen(
                            viewState = model.viewState.collectAsStateWithLifecycle().value,
                            onGesture = model::process,
                            onTerminated = {
                                findNavController().popBackStack()
                            },
                            onLogin = {
                                findNavController().navigate(links.login())
                            }
                        )
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "RecipeFragment"
    }
}