package com.motorro.di

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.di.databinding.FragmentVmBinding
import com.motorro.di.timer.Timer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ViewModelFragment : Fragment(), WithViewBinding<FragmentVmBinding> by BindingHost() {
    private lateinit var timer1: Timer
    private lateinit var timer2: Timer
    private val timer3: Timer by viewModels<TimerViewModel>()

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentVmBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        withBinding {
            timer1.setTimer(this@ViewModelFragment.timer1)
            timer2.setTimer(this@ViewModelFragment.timer2)
            timer3.setTimer(this@ViewModelFragment.timer3)
        }
    }

    companion object {
        private val TAG: String = "ViewModelFragment"
    }
}