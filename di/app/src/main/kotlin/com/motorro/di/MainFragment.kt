package com.motorro.di

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.di.databinding.FragmentMainBinding
import com.motorro.di.timer.Timer
import com.motorro.di.timer.TimerImplementation

class MainFragment : Fragment(), WithViewBinding<FragmentMainBinding> by BindingHost() {

    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timer = TimerImplementation(
            title = "Fragment time",
            scope = lifecycleScope,
            delayMillis = 100
        )
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