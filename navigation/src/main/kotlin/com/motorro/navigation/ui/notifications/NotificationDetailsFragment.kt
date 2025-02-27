package com.motorro.navigation.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.motorro.navigation.data.notifications
import com.motorro.navigation.databinding.FragmentNotificationDetailsBinding

class NotificationDetailsFragment : Fragment() {
    companion object {
        private const val NOTIFICATION_ID = "notificationId"

        fun newInstance(notificationId: Int) = NotificationDetailsFragment().apply {
            arguments = Bundle().apply {
                putInt(NOTIFICATION_ID, notificationId)
            }
        }
    }

    private val notificationId: Int get() = requireNotNull(arguments?.getInt(NOTIFICATION_ID)?.takeIf { it > 0 }) {
        "Argument $NOTIFICATION_ID is missing"
    }

    private lateinit var binding: FragmentNotificationDetailsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNotificationDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val notification = notifications.getValue(notificationId)
        binding.topAppBar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.title.text = notification
        binding.topAppBar.title = notification
    }
}