package com.motorro.cookbook.addrecipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.motorro.cookbook.addrecipe.databinding.FragmentAddRecipeBinding
import com.motorro.cookbook.appcore.viewbinding.BindingHost
import com.motorro.cookbook.appcore.viewbinding.WithViewBinding
import com.motorro.cookbook.appcore.viewbinding.bindView
import com.motorro.cookbook.appcore.viewbinding.withBinding
import kotlinx.coroutines.launch

class AddRecipeFragment : Fragment(), WithViewBinding<FragmentAddRecipeBinding> by BindingHost() {
    private val model by viewModels<AddRecipeFragmentViewModel> {
        AddRecipeFragmentViewModel.Factory(requireContext())
    }

    private val selectPicture = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        model.setImage(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = bindView(
        container,
        FragmentAddRecipeBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            topAppBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
        setupTitle()
        setupImage()
        setupCategory()
        setupSteps()
        setupSaveButton()
    }

    private fun setupTitle() = withBinding {
        viewLifecycleOwner.lifecycleScope.launch {
            model.title.collect {
                title.setTextKeepState(it)
            }
        }
        title.doOnTextChanged { text, _, _, _ ->
            model.setTitle(text.toString())
        }
    }

    private fun setupImage() = withBinding {
        viewLifecycleOwner.lifecycleScope.launch {
            model.image.collect { pic ->
                if (null != pic) {
                    Glide.with(this@withBinding.root)
                        .load(pic)
                        .centerCrop()
                        .into(image)
                } else {
                    image.setImageResource(R.drawable.ic_add)
                }
            }
        }
        image.setOnClickListener {
            selectPicture.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun setupCategory() = withBinding {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.categories.collect { categories ->
                    category.setAdapter(
                        ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
                    )
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            model.category.collect {
                category.setTextKeepState(it)
            }
        }
        category.doOnTextChanged { text, _, _, _ ->
            model.setCategory(text.toString())
        }
    }

    private fun setupSteps() = withBinding {
        viewLifecycleOwner.lifecycleScope.launch {
            model.description.collect {
                description.setTextKeepState(it)
            }
        }
        description.doOnTextChanged { text, _, _, _ ->
            model.setSteps(text.toString())
        }
    }

    private fun setupSaveButton() = withBinding {
        viewLifecycleOwner.lifecycleScope.launch {
            model.saveEnabled.collect { enabled ->
                save.isEnabled = enabled
            }
        }
        save.setOnClickListener {
            model.save()
            findNavController().popBackStack()
        }
    }
}