package com.motorro.sqlite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.sqlite.databinding.FragmentTagBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class TagFragment : Fragment(), WithViewBinding<FragmentTagBinding> by BindingHost() {

    private val model: TagViewModel by viewModels {
        TagViewModel.Factory(
            requireApp(),
            TagFragmentArgs.fromBundle(requireArguments()).tagId
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return bindView(container, FragmentTagBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        withBinding {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    supervisorScope {
                        launch {
                            model.name.collect { n ->
                                tagName.setTextKeepState(n.first)
                                tagName.error = if (n.second) getString(R.string.error_tag_exists) else null
                            }
                        }
                        launch {
                            model.description.collect { d ->
                                tagDescription.setTextKeepState(d)
                            }
                        }
                        launch {
                            model.saveEnabled.collect { enabled ->
                                save.isEnabled = enabled
                            }
                        }
                        launch {
                            model.complete.collect { c ->
                                if (c) {
                                    findNavController().navigateUp()
                                }
                            }
                        }
                    }
                }
            }
            topAppBar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
            tagName.doAfterTextChanged {
                model.setName(it.toString())
            }
            tagDescription.doAfterTextChanged {
                model.setDescription(it.toString())
            }
            save.setOnClickListener {
                model.save()
            }
        }
    }

    companion object {
        const val RESULT = "result"
    }
}