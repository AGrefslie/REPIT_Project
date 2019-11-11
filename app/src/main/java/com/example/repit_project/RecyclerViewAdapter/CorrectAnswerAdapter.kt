package com.example.repit_project.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repit_project.R
import kotlinx.android.synthetic.main.layout_listitem_correct_answer.view.*

class CorrectAnswerAdapter(private val list: ArrayList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items : ArrayList<String> = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QuestionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem_correct_answer, parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is QuestionViewHolder -> {
                holder.bind(items.get(position))

                val currentQuestion = items[position]
                holder.bind(currentQuestion)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(QuestionList : ArrayList<String>) {
        items = QuestionList
    }

    class QuestionViewHolder constructor (itemView : View): RecyclerView.ViewHolder(itemView) {

        val question = itemView.question

        fun bind(Question : String) {

            question.setText(Question)

        }
    }
}