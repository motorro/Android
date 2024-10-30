package com.motorro.activity.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.motorro.activity.data.Email
import com.motorro.activity.databinding.ItemEmailBinding

/**
 * Email adapter
 */
class EmailAdapter(private val onClick: (Int) -> Unit) : ListAdapter<Email, EmailAdapter.EmailHolder>(EmailDiff) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EmailHolder(
        ItemEmailBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        onClick
    )

    override fun onBindViewHolder(holder: EmailHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class EmailHolder(private val binding: ItemEmailBinding, private val onClick: (Int) -> Unit) : ViewHolder(binding.root) {
        private var id: Int = -1

        fun bind(email: Email) = with(binding) {
            id = email.id
            subject.text = email.subject
            address.text = email.address
            date.text = email.created.formatLocal()
            root.setOnClickListener { onClick(id) }
        }
    }
}

private object EmailDiff : DiffUtil.ItemCallback<Email>() {
    override fun areItemsTheSame(oldItem: Email, newItem: Email): Boolean {
        return oldItem.id == newItem.id
    }
    override fun areContentsTheSame(oldItem: Email, newItem: Email): Boolean {
        return oldItem == newItem
    }
}