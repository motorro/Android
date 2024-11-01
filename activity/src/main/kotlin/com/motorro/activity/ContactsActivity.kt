package com.motorro.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.motorro.activity.data.Contact
import com.motorro.activity.data.contactList
import com.motorro.activity.databinding.ActivityContactsBinding
import com.motorro.activity.ui.ContactsAdapter
import com.motorro.activity.ui.MarginDecorator

/**
 * Contacts activity
 */
class ContactsActivity : AppCompatActivity() {
    companion object {
        private const val EXTRA_SEARCH = "search"
        private const val EXTRA_CONTACT = "contact"
    }

    /**
     * Contract to pick a contact
     */
    class PickContactContract : ActivityResultContract<String, Contact?>() {
        /**
         * Create intent to start activity
         */
        override fun createIntent(context: Context, input: String): Intent {
            return Intent(context, ContactsActivity::class.java).apply {
                putExtra(EXTRA_SEARCH, input)
            }
        }

        /**
         * Convert result obtained from contacts activity.
         */
        @Suppress("DEPRECATION")
        override fun parseResult(resultCode: Int, intent: Intent?): Contact? {
            if (resultCode != Activity.RESULT_OK) {
                return null
            }
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                intent?.getParcelableExtra(EXTRA_CONTACT, Contact::class.java)
            } else {
                intent?.getParcelableExtra(EXTRA_CONTACT)
            }
        }
    }

    private val adapter = ContactsAdapter { contact ->
        // Return selected contact
        setResult(Activity.RESULT_OK, Intent().apply {
            // Contact is parcelable - so we can pass it as is
            putExtra(EXTRA_CONTACT, contact)
        })
        finish()
    }

    private lateinit var binding: ActivityContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.topAppBar.setNavigationOnClickListener {
            // Cancel selection
            setResult(Activity.RESULT_CANCELED)
            finish()
        }

        setupRecycler()
    }

    /**
     * Sets up recycler view
     */
    private fun setupRecycler() = with(binding) {
        list.adapter = adapter
        list.addItemDecoration(MarginDecorator(resources.getDimensionPixelSize(R.dimen.margin)))

        // Take input data from intent and filter contacts
        var contacts = contactList
        val toSearch = intent.getStringExtra(EXTRA_SEARCH) ?: ""
        if (toSearch.isNotBlank()) {
            contacts = contactList.filter {
                it.email.contains(toSearch, ignoreCase = true) || it.name.contains(toSearch, ignoreCase = true)
            }
        }
        adapter.submitList(contacts)
    }
}