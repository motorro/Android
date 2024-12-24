package com.motorro.recyclerview.ui.staggered

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.motorro.recyclerview.databinding.VhLetterBinding
import com.motorro.recyclerview.ui.staggered.data.Letter

class LettersAdapter() : RecyclerView.Adapter<LettersAdapter.LetterViewHolder>() {

    private var letters: List<Letter> = emptyList()

    fun setLetters(letters: List<Letter>) {
        this.letters = letters
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        return LetterViewHolder(VhLetterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return letters.size
    }

    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        holder.bind(letters[position])
    }

    class LetterViewHolder(private val binding: VhLetterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(let: Letter) = with(binding) {
            letter.text = let.symbol.toString()
            letter.textSize = 14f + let.count * 2
            count.text = let.count.toString()
        }
    }
}