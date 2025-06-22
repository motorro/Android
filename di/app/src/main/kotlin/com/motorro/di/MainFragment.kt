package com.motorro.di

import android.os.Bundle
import android.util.Log
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
import com.motorro.di.timer.TimerImplementation
import javax.inject.Inject

class MainFragment : Fragment(), WithViewBinding<FragmentMainBinding> by BindingHost() {

    private lateinit var timer: Timer

    @Inject
    fun setTimer(timer: TimerImplementation) {
        Log.i(TAG, "Injecting timer...")
        this.timer = timer
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "About to inject fragment...")
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

    companion object {
        private val TAG: String = "MainFragment"
    }
}