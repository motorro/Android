package com.motorro.flow

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.motorro.flow.data.Note
import com.motorro.flow.data.Tag
import com.motorro.flow.data.User
import com.motorro.flow.data.getNotesFlow
import com.motorro.flow.data.getTagsForUser
import com.motorro.flow.data.getUsers
import com.motorro.flow.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        with(binding) {
            groupUsers.setOnCheckedStateChangeListener { _, ids ->
                val chipId = ids.firstOrNull()
                if (null == chipId) {
                    selectUser(null)
                    return@setOnCheckedStateChangeListener
                }
                val userId = groupUsers.findViewById<Chip>(chipId).tag as Int
                selectUser(userId)
            }
            groupTags.setOnCheckedStateChangeListener { _, ids ->
                val tags = ids
                    .map { id -> groupTags.findViewById<Chip>(id).tag as Int }
                    .toSet()
                selectTags(tags)
            }
            adapter = NotesAdapter()
            groupNotes.adapter = adapter
        }

        loadUsers()
    }

    private fun populateUsers(users: List<User>) {
        val group = binding.groupUsers
        group.removeAllViews()
        users.forEach {
            val chip = Chip(group.context).apply {
                text = it.name
                tag = it.id
                isClickable = true
                isCheckable = true
                isFocusable = true
            }
            group.addView(chip)
        }
    }

    private fun populateTags(tags: List<Tag>) {
        val group = binding.groupTags
        group.removeAllViews()
        tags.forEach {
            val chip = Chip(group.context).apply {
                text = it.name
                tag = it.id
                isClickable = true
                isCheckable = true
                isFocusable = true
            }
            group.addView(chip)
        }
    }

    private fun populateNotes(notes: List<Note>) {
        Log.i(TAG, "Populating notes: $notes")
        adapter.submitList(notes)
    }

    private fun hideTags() {
        binding.controlsTags.visibility = android.view.View.GONE
    }

    private fun showTags() {
        binding.controlsTags.visibility = android.view.View.VISIBLE
    }

    // region Logic

    private fun getSelectedUser(): Int? {
        val chipId = binding.groupUsers.checkedChipId
        return if (chipId == android.view.View.NO_ID) {
            null
        } else {
            binding.groupUsers.findViewById<Chip>(chipId).tag as Int
        }
    }

    private fun getSelectedTags(): Set<Int> {
        return binding.groupTags.checkedChipIds
            .map { id -> binding.groupTags.findViewById<Chip>(id).tag as Int }
            .toSet()
    }

    private fun loadUsers() {
        Log.i(TAG, "Loading users")
        lifecycleScope.launch {
            val result = getUsers()
            if (result.isSuccess) {
                populateUsers(result.getOrThrow())
            } else {
                Log.e(TAG, "Failed to load users", result.exceptionOrNull())
            }
        }
    }

    private fun selectUser(userId: Int?) {
        Log.i(TAG, "Selected user: $userId")
        loadTags()
    }

    private fun loadTags() {
        val userId = getSelectedUser()
        Log.i(TAG, "Loading tags for user: $userId")
        hideTags()
        populateNotes(emptyList())
        if (null == userId) {
            populateTags(emptyList())
            return
        }

        lifecycleScope.launch {
            val result = getTagsForUser(userId)
            if (result.isSuccess) {
                populateTags(result.getOrThrow())
                showTags()
                loadNotes()
            } else {
                Log.e(TAG, "Failed to load tags", result.exceptionOrNull())
            }
        }
    }

    private fun selectTags(tags: Set<Int>) {
        Log.i(TAG, "Selected tags: $tags")
        loadNotes()
    }

    private fun loadNotes() {
        val userId = getSelectedUser()
        val tags = getSelectedTags()
        Log.i(TAG, "Loading notes for user: $userId and tags: $tags")

        if (null == userId) {
            populateNotes(emptyList())
            return
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                getNotesFlow(userId, tags).collect {
                    populateNotes(it)
                }
            }
        }
    }

    // endregion

    companion object {
        private const val TAG = "MainActivity"
    }
}