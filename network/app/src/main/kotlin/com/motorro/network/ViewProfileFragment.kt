package com.motorro.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.network.databinding.FragmentViewProfileBinding
import kotlinx.coroutines.launch

class ViewProfileFragment : Fragment(), WithViewBinding<FragmentViewProfileBinding> by BindingHost() {
    private val model: ProfileViewModel by viewModels {
        ProfileViewModel.Factory(
            userId = ViewProfileFragmentArgs.fromBundle(requireArguments()).userId,
            app = app()
        )
    }
    private val imageTransformation by lazy {
        MultiTransformation(
            CenterCrop(),
            RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.rounded_corners))
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentViewProfileBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.profile.collect { profile ->
                    withBinding {
                        when (profile) {
                            null -> {
                                progress.isVisible = true
                                widgets.isVisible = false
                            }
                            else -> {
                                progress.isVisible = false
                                widgets.isVisible = true
                                name.text = profile.name
                                Glide.with(this@ViewProfileFragment).load(profile.userpic.toString()).transform(imageTransformation).into(image)
                            }
                        }
                    }
                }
            }
        }
    }
}