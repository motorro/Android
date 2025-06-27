package com.motorro.di

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.di.databinding.FragmentMainBinding
import com.motorro.di.timer.Timer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainFragment : Fragment(), WithViewBinding<FragmentMainBinding> by BindingHost() {

    private lateinit var timer1: Timer
    private lateinit var timer2: Timer
    private lateinit var timer3: Timer

    @Inject
    fun setTimer1(@Named("app") timer: Timer) {
        Log.i(TAG, "Injecting app timer: $timer")
        this.timer1 = timer
    }

    @Inject
    fun setTimer2(@Named("activity") timer: Timer) {
        Log.i(TAG, "Injecting activity timer: $timer")
        this.timer2 = timer
    }

    @Inject
    fun setTimer3(@Named("fragment") timer: Timer) {
        Log.i(TAG, "Injecting fragment timer: $timer")
        this.timer3 = timer
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentMainBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            timer1.setTimer(this@MainFragment.timer1)
            timer2.setTimer(this@MainFragment.timer2)
            timer3.setTimer(this@MainFragment.timer3)
            btnProvide.setOnClickListener {
                findNavController().navigate(
                    MainFragmentDirections.mainToStatus(this@MainFragment.timer1.count.value.inWholeMilliseconds)
                )
            }
        }
    }

    companion object {
        private val TAG: String = "MainFragment"
    }
}