package com.motorro.architecture.account

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.motorro.architecture.appcore.account.AuthenticationProvider
import com.motorro.architecture.appcore.viewbinding.BindingHost
import com.motorro.architecture.appcore.viewbinding.WithViewBinding
import com.motorro.architecture.appcore.viewbinding.withBinding
import com.motorro.architecture.databinding.VhAccountProviderBinding

/**
 * Adapter for authentication providers
 */
class AuthenticationProviderAdapter(
    private val providers: List<AuthenticationProvider>,
    private val onClick: (AuthenticationProvider) -> Unit
) : RecyclerView.Adapter<AuthenticationProviderAdapter.ViewHolder>() {

    override fun getItemCount(): Int = providers.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        VhAccountProviderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(providers[position])
    }

    inner class ViewHolder(binding: VhAccountProviderBinding): RecyclerView.ViewHolder(binding.root), WithViewBinding<VhAccountProviderBinding> by BindingHost() {
        private lateinit var provider: AuthenticationProvider

        init {
            this.binding = binding
            withBinding {
                root.setOnClickListener {
                    onClick(provider)
                }
            }
        }

        fun bind(provider: AuthenticationProvider) = withBinding {
            this@ViewHolder.provider = provider
            imgLogo.setImageResource(provider.iconResource)
            imgLogo.contentDescription
            txtTitle.setText(provider.title)
        }
    }
}