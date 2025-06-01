package com.motorro.architecture.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.bindView
import com.motorro.architecture.appcore.viewbinding.withBinding
import com.motorro.architecture.content.di.ContentFragmentContainer
import com.motorro.architecture.core.error.CoreException
import com.motorro.architecture.core.lce.LceState
import com.motorro.architecture.core.log.Logging
import com.motorro.architecture.databinding.FragmentContentBinding
import com.motorro.architecture.main.di.ProvidesMainActivityContainer
import com.motorro.architecture.model.user.UserProfile
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ContentFragment : Fragment(), WithViewBinding<FragmentContentBinding> by BindingHost(), Logging {

    private lateinit var fragmentComponent: ContentFragmentContainer

    override val defaultViewModelProviderFactory: ViewModelProvider.Factory get() = fragmentComponent.contentFragmentModelFactory

    private val viewModel: ContentViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentComponent = ContentFragmentContainer((requireActivity() as ProvidesMainActivityContainer).activityContainer)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentContentBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            error.setOnDismissListener { _, error ->
                if (error.isFatal) {
                    i { "Dismissing fatal error. Closing app..." }
                    requireActivity().finish()
                } else {
                    i { "Dismissing non-fatal error. Retrying..." }
                    viewModel.retry()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.onEach(::processState).launchIn(this)
        }
    }

    private fun processState(state: LceState<UserProfile, CoreException>) = withBinding {
        when(state) {
            is LceState.Content -> {
                d { "Content." }
                progress.hide()
                error.isVisible = false
                content.isVisible = true
                setContent(state.data)
            }
            is LceState.Error -> {
                w { "Error. Has data: ${ null != state.data }" }
                progress.hide()
                val data = state.data
                if (null != data) {
                    content.isVisible = true
                    error.isVisible = false
                    setContent(data)
                } else {
                    content.isVisible = false
                    error.isVisible = true
                    error.error = state.error
                }
            }
            is LceState.Loading -> {
                progress.isVisible = true
                error.isVisible = false
                val data = state.data
                if (null != data) {
                    content.isVisible = true
                    setContent(data)
                } else {
                    content.isVisible = false
                }
            }
        }
    }

    private fun setContent(data: UserProfile) = withBinding {
        textUsername.text = data.displayName
        textCountry.text = fragmentComponent.countryRegistry.getCountry(data.countryCode)
    }
}