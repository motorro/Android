package com.motorro.activity

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import com.motorro.activity.data.Contact

/**
 * Contacts loader
 */
class ContactsLoader(private val context: Context) {

    /**
     * Uses content-provider to get a list of contacts
     */
    fun getContacts(search: String): List<Contact> {
        val uri: Uri = ContactsContract.CommonDataKinds.Email.CONTENT_URI
        val cursor: Cursor? = context.contentResolver.query(
            uri,
            FROM_COLUMNS,
            SELECTION.takeIf { search.isNotBlank() } ,
            search.takeIf { search.isNotBlank() }?.let { arrayOf("%$it%") },
            ORDER
        )

        if (null == cursor) {
            return emptyList()
        }

        val data = mutableListOf<Contact>()

        cursor.use { cur ->
            while (cur.moveToNext()) {
                data.add(
                    Contact(
                        id = cur.getLong(CONTACT_ID_INDEX),
                        name = cur.getString(CONTACT_NAME_INDEX).orEmpty(),
                        email = cur.getString(CONTACT_EMAIL_INDEX)
                    )
                )
            }
        }

        return data
    }

    companion object {
        private const val CONTACT_ID_INDEX: Int = 0
        private const val CONTACT_NAME_INDEX: Int = 1
        private const val CONTACT_EMAIL_INDEX: Int = 2
        private val FROM_COLUMNS: Array<String> = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.CommonDataKinds.Email.DATA
        )
        private const val SELECTION: String = "${ContactsContract.Contacts.DISPLAY_NAME_PRIMARY} LIKE ? AND ${ContactsContract.CommonDataKinds.Email.DATA} NOT LIKE ''"
        private const val ORDER = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
    }
}

/**
 * Intermediate contact data
 */
data class ListContact(
    val id: Long,
    val name: String,
    val lookupKey: String
)