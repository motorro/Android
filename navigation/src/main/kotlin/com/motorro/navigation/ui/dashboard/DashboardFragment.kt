package com.motorro.navigation.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.motorro.navigation.R
import com.motorro.navigation.data.sessionManager
import com.motorro.navigation.databinding.FragmentDashboardBinding
import com.motorro.navigation.ui.WelcomeFragment

class DashboardFragment : Fragment() {

    companion object {
        /**
         * Create a new instance of DashboardFragment.
         */
        fun newInstance(): DashboardFragment = DashboardFragment()
    }

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val sessionManager by sessionManager()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.logout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        sessionManager.logout()
        requireParentFragment().parentFragmentManager.commit {
            replace(R.id.fragment_container_view, WelcomeFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}