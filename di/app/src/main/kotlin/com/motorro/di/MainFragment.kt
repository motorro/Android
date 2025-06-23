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
import javax.inject.Inject

class MainFragment : Fragment(), WithViewBinding<FragmentMainBinding> by BindingHost() {

    private lateinit var timer1: Timer
    private lateinit var timer2: Timer

    @Inject
    fun setTimer1(timer: Timer) {
        Log.i(TAG, "Injecting timer 1: $timer")
        this.timer1 = timer
    }

    @Inject
    fun setTimer2(timer: Timer) {
        Log.i(TAG, "Injecting timer 2: $timer")
        this.timer2 = timer
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
            timer1.setTimer(this@MainFragment.timer1)
            timer2.setTimer(this@MainFragment.timer2)
        }
    }

    companion object {
        private val TAG: String = "MainFragment"
    }
}