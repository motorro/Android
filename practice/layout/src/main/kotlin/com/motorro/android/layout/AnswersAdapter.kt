package com.motorro.android.layout

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class AnswersAdapter : ListAdapter<Answer, AnswersAdapter.Holder>(AnswersDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(
        LayoutInflater.from(parent.context).inflate(R.layout.vh_word, parent, false)
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val word: TextView = itemView as TextView

        fun bind(answer: Answer) {
            word.text = answer.word

            val color: Int
            val bg: Int
            val font: Int
            if (answer.correct) {
                color = R.color.correct
                bg = R.drawable.shape_variant_word_bg_correct
                font = R.font.nunito_black
            } else {
                color = R.color.black
                bg = R.drawable.shape_variant_word_bg
                font = R.font.nunito_bold
            }

            word.setTextColor(ContextCompat.getColor(word.context, color))
            word.background = ContextCompat.getDrawable(word.context, bg)
            word.typeface = word.context.resources.getFont(font)
        }
    }
}

object AnswersDiffCallback : DiffUtil.ItemCallback<Answer>() {
    override fun areItemsTheSame(oldItem: Answer, newItem: Answer): Boolean {
        return oldItem.word == newItem.word
    }

    override fun areContentsTheSame(oldItem: Answer, newItem: Answer): Boolean {
        return oldItem == newItem
    }
}