package com.motorro.cookbook.ui

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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.motorro.cookbook.R
import com.motorro.cookbook.databinding.FragmentAddRecipeBinding

class AddRecipeFragment : Fragment() {
    private val binding = FragmentBindingDelegate<FragmentAddRecipeBinding>(this)
    private val model by viewModels<AddRecipeFragmentViewModel> {
        AddRecipeFragmentViewModel.Factory(requireContext())
    }

    private val selectPicture = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        model.setImage(uri?.toString())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.bind(
        container,
        FragmentAddRecipeBinding::inflate
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitle()
        setupImage()
        setupCategory()
        setupSteps()
        setupSaveButton()
        binding.withBinding {
            topAppBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun setupTitle() = binding.withBinding {
        model.title.observe(viewLifecycleOwner) {
            title.setTextKeepState(it)
        }
        title.doOnTextChanged { text, _, _, _ ->
            model.setTitle(text.toString())
        }
    }

    private fun setupImage() = binding.withBinding {
        model.image.observe(viewLifecycleOwner) { pic ->
            if (null != pic) {
                Glide.with(this.root)
                    .load(pic)
                    .centerCrop()
                    .into(image)
            } else {
                image.setImageResource(R.drawable.ic_add)
            }
        }
        image.setOnClickListener {
            selectPicture.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }
    }

    private fun setupCategory() = binding.withBinding {
        model.categories.observe(viewLifecycleOwner) { categories ->
            category.setAdapter(
                ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
            )
        }

        model.category.observe(viewLifecycleOwner) {
            category.setTextKeepState(it)
        }

        category.doOnTextChanged { text, _, _, _ ->
            model.setCategory(text.toString())
        }
    }

    private fun setupSteps() = binding.withBinding {
        model.steps.observe(viewLifecycleOwner) {
            steps.setTextKeepState(it)
        }
        steps.doOnTextChanged { text, _, _, _ ->
            model.setSteps(text.toString())
        }
    }

    private fun setupSaveButton() = binding.withBinding {
        model.saveEnabled.observe(viewLifecycleOwner) { enabled ->
            save.isEnabled = enabled
        }
        save.setOnClickListener {
            model.save()
            findNavController().popBackStack()
        }
    }
}