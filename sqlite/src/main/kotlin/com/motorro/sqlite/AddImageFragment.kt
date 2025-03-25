package com.motorro.sqlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.sqlite.databinding.FragmentAddBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.datetime.format

class AddImageFragment : Fragment(), WithViewBinding<FragmentAddBinding> by BindingHost() {

    private val model: AddImageViewModel by viewModels {
        AddImageViewModel.Factory(requireApp(), AddImageFragmentArgs.fromBundle(requireArguments()).image)
    }
    private val imageTransformation by lazy {
        MultiTransformation(
            CenterCrop(),
            RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.rounded_corners))
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindView(container, FragmentAddBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (model.complete.value) {
            findNavController().navigateUp()
            return
        }

        withBinding {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    supervisorScope {
                        launch {
                            model.image.collect { u ->
                                Glide.with(this@AddImageFragment).load(u).transform(imageTransformation).into(image)
                            }
                        }
                        launch {
                            model.dateTaken.collect { dt ->
                                dateTaken.text = dt?.format(dateFormat)
                            }
                        }
                        launch {
                            model.title.collect { t ->
                                title.setTextKeepState(t.orEmpty())
                            }
                        }
                        launch {
                            model.saveEnabled.collect { se ->
                                save.isEnabled = se
                            }
                        }
                        launch {
                            model.complete.collect { c ->
                                if (c) {
                                    findNavController().navigateUp()
                                }
                            }
                        }
                    }
                }
            }
            topAppBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            title.doAfterTextChanged {
                model.setTitle(it.toString())
            }
            save.setOnClickListener {
                model.save()
            }
        }
    }
}