package com.motorro.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.motorro.activity.data.Contact
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

    /**
     * Request permission to read calendar
     */
    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            // Permission granted - get contacts
            getContacts()
        } else {
            // Permission denied - show snackbar with settings link
            // to allow user to change permissions
            showSettingsPrompt()
        }
    }

    /**
     * Launch permission request
     */
    private fun launchPermissionRequest() {
        permissionLauncher.launch(android.Manifest.permission.READ_CONTACTS)
    }

    /**
     * Check if we have permission to read contacts
     */
    private fun checkPermissions() {
        // 1. Check if we have permission to read contacts
        val granted = PackageManager.PERMISSION_GRANTED == checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
        if (granted) {
            getContacts()
            return
        }

        // 2. If not - check if we should show rationale
        //    Rationale is shown if user denied permission before (once)
        if(shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)) {
            showRationale()
            return
        }

        // 3. Launch permission request
        launchPermissionRequest()
    }

    /**
     * Ask user for contacts permission if he refused before
     */
    private fun showRationale() = askUser(R.string.contacts_rationale) {
        launchPermissionRequest()
    }

    /**
     * Ask user to go to settings if he refused before
     */
    private fun showSettingsPrompt() = askUser(R.string.contacts_settings) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = android.net.Uri.parse("package:$packageName")
        startActivity(intent)
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
    private lateinit var loader: ContactsLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loader = ContactsLoader(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.topAppBar.setNavigationOnClickListener {
            // Cancel selection
            cancel()
        }

        setupRecycler()
    }

    /**
     * Check permissions on start
     */
    override fun onStart() {
        super.onStart()
        checkPermissions()
    }

    /**
     * Sets up recycler view
     */
    private fun setupRecycler() = with(binding) {
        list.adapter = adapter
        list.addItemDecoration(MarginDecorator(resources.getDimensionPixelSize(R.dimen.margin)))
    }

    /**
     * Get contacts
     */
    private fun getContacts() {
        // Take input data from intent and filter contacts
        val toSearch = intent.getStringExtra(EXTRA_SEARCH) ?: ""
        adapter.submitList(loader.getContacts(toSearch))
    }

    private fun cancel() {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    private fun askUser(@StringRes message: Int, action: () -> Unit) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.btn_ok) { action() }
            .show()
    }
}