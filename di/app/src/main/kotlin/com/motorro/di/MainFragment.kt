package com.motorro.di

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.di.databinding.FragmentMainBinding
import com.motorro.di.di.ProvidesApplicationComponent
import com.motorro.di.timer.Timer
import javax.inject.Inject

class MainFragment : Fragment(), WithViewBinding<FragmentMainBinding> by BindingHost() {

    @set:Inject
    lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireActivity().application as ProvidesApplicationComponent).applicationComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentMainBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            fragmentTimer.setTimer(timer)
        }
    }
}