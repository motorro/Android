package com.motorro.recyclerview.ui.grid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.motorro.recyclerview.databinding.VhPictureBinding
import com.motorro.recyclerview.ui.grid.data.Picture

class PicturesAdapter() : RecyclerView.Adapter<PicturesAdapter.PictureViewHolder>() {

    private var pictures: List<Picture> = emptyList()

    fun setPictures(pictures: List<Picture>) {
        this.pictures = pictures
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        return PictureViewHolder(VhPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return pictures.size
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        holder.bind(pictures[position])
    }

    class PictureViewHolder(private val binding: VhPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pic: Picture) = with(binding) {
            picture.setImageResource(pic.picture)
            title.text = pic.title
        }
    }
}