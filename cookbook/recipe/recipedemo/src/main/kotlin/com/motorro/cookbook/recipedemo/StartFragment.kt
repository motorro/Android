package com.motorro.cookbook.recipedemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.motorro.cookbook.appcore.navigation.Links
import com.motorro.cookbook.appcore.viewbinding.BindingHost
import com.motorro.cookbook.appcore.viewbinding.WithViewBinding
import com.motorro.cookbook.appcore.viewbinding.bindView
import com.motorro.cookbook.appcore.viewbinding.withBinding
import com.motorro.cookbook.data.recipes.RECIPES
import com.motorro.cookbook.recipedemo.databinding.FragmentStartBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StartFragment : Fragment(), WithViewBinding<FragmentStartBinding> by BindingHost() {
    @set:Inject
    lateinit var links: Links

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindView(container, FragmentStartBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            btnStart.setOnClickListener {
                findNavController().navigate(links.recipe(RECIPES.first().id))
            }
        }
    }
}