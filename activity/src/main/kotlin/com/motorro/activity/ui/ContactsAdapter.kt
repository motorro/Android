package com.motorro.activity.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.motorro.activity.data.Contact
import com.motorro.activity.databinding.ItemContactBinding

/**
 * Contact adapter
 */
class ContactsAdapter(private val onClick: (Contact) -> Unit) : ListAdapter<Contact, ContactsAdapter.ContactHolder>(ContactDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactHolder(
        ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ContactHolder(private val binding: ItemContactBinding, private val onClick: (Contact) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) = with(binding) {
            name.text = contact.name
            address.text = contact.email
            root.setOnClickListener { onClick(contact) }
        }
    }
}

private object ContactDiff : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}