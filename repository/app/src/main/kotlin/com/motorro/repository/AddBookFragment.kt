package com.motorro.repository

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.motorro.core.viewbinding.BindingHost
import com.motorro.core.viewbinding.WithViewBinding
import com.motorro.core.viewbinding.bindView
import com.motorro.core.viewbinding.withBinding
import com.motorro.repository.data.AddBookViewState
import com.motorro.repository.data.getCover
import com.motorro.repository.databinding.FragmentAddBookBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AddBookFragment : Fragment(), WithViewBinding<FragmentAddBookBinding> by BindingHost() {
    private val model: AddBookViewModel by viewModels {
        AddBookViewModel.Factory(app = app())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return bindView(container, FragmentAddBookBinding::inflate)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        withBinding {
            prevPicture.setOnClickListener {
                model.previousCover()
            }
            nextPicture.setOnClickListener {
                model.nextCover()
            }
            title.doAfterTextChanged {
                model.updateTitle(it.toString())
            }
            authors.doAfterTextChanged {
                model.updateAuthors(it.toString())
            }
            desc.doAfterTextChanged {
                model.updateSummary(it.toString())
            }
            save.setOnClickListener {
                model.save()
            }
            btnRetry.setOnClickListener {
                model.save()
            }
        }

        viewLifecycleOwner.lifecycle.coroutineScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                model.viewState.onEach(::processDataState).launchIn(this)
            }
        }
    }

    private fun processDataState(state: AddBookViewState) {
        updateForm(state.form)
        when (state) {
            is AddBookViewState.Editing -> {
                withBinding {
                    progress.hide()
                    contentGroup.visibility = View.VISIBLE
                    errorGroup.visibility = View.GONE
                    prevPicture.isEnabled = true
                    nextPicture.isEnabled = true
                    title.isEnabled = true
                    authors.isEnabled = true
                    desc.isEnabled = true
                    save.isEnabled = state.saveEnabled
                }
            }
            is AddBookViewState.Loading -> {
                withBinding {
                    progress.show()
                    contentGroup.visibility = View.VISIBLE
                    errorGroup.visibility = View.GONE
                    prevPicture.isEnabled = false
                    nextPicture.isEnabled = false
                    title.isEnabled = false
                    authors.isEnabled = false
                    desc.isEnabled = false
                    save.isEnabled = false
                }
            }
            is AddBookViewState.Error -> {
                withBinding {
                    progress.hide()
                    contentGroup.visibility = View.GONE
                    errorGroup.visibility = View.VISIBLE
                }
            }
            is AddBookViewState.Saved -> {
                findNavController().navigateUp()
            }
        }
    }

    private fun updateForm(form: AddBookViewState.Form) {
        withBinding {
            Glide.with(requireContext())
                .load(getCover(form.coverId))
                .into(cover)
            title.setTextKeepState(form.title)
            authors.setTextKeepState(form.authors)
            desc.setTextKeepState(form.summary)
        }
    }
}