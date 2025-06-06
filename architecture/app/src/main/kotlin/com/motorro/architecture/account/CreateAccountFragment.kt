package com.motorro.architecture.account

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.motorro.architecture.account.di.CreateAccountFragmentContainer
import com.motorro.architecture.account.di.buildContentFragmentContainer
import com.motorro.architecture.appcore.R
import com.motorro.architecture.appcore.di.ProvidesFragmentContainer
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.appcore.viewbinding.withBinding
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.databinding.FragmentCreateAccountBinding

class CreateAccountFragment : Fragment(), ProvidesFragmentContainer, WithViewBinding<FragmentCreateAccountBinding> by BindingHost(), Logging {
    override lateinit var fragmentContainer: CreateAccountFragmentContainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentContainer = buildContentFragmentContainer()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentCreateAccountBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            providers.adapter = fragmentContainer.adapterFactory { provider ->
                d { "Authenticating  with ${provider.title}" }
                findNavController().navigate(provider.route)
            }
            providers.addItemDecoration(MarginItemDecoration(resources.getDimensionPixelSize(R.dimen.vertical_padding)))
        }
    }
}

class MarginItemDecoration(private val spaceSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        with(outRect) {
            if (parent.getChildAdapterPosition(view) == 0) {
                top = spaceSize
            }
            left = spaceSize
            right = spaceSize
            bottom = spaceSize
        }
    }
}

