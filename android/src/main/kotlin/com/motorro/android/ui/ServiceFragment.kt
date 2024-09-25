package com.motorro.android.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.motorro.android.PlayerService
import com.motorro.android.databinding.FragmentServiceBinding

class ServiceFragment : Fragment() {
    private var _binding: FragmentServiceBinding? = null
    private val binding get() = _binding!!

    private lateinit var permissionRequestLauncher: ActivityResultLauncher<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentServiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun enableService() = with(binding) {
        startService.isEnabled = true
        stopService.isEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        permissionRequestLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                enableService()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please allow this app to post notifications",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        startService.setOnClickListener {
            val context = requireContext()
            context.startForegroundService(Intent(
                context,
                PlayerService::class.java
            ))
        }
        stopService.setOnClickListener {
            val context = requireContext()
            context.stopService(Intent(
                context,
                PlayerService::class.java
            ))
        }
    }

    override fun onStart() {
        super.onStart()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)) {
                enableService()
            } else {
                permissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            enableService()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}