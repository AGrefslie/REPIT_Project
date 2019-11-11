package com.example.repit_project.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repit_project.Models.Question
import com.example.repit_project.R
import kotlinx.android.synthetic.main.layout_listitem_wrong_answer.view.*

class WrongAnswerAdapter(private val list: ArrayList<Question>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items : ArrayList<Question> = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QuestionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem_wrong_answer, parent, false)
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

    fun submitList(QuestionList : ArrayList<Question>) {
        items = QuestionList
    }

    class QuestionViewHolder constructor (itemView : View): RecyclerView.ViewHolder(itemView) {

        val question = itemView.question
        val answer = itemView.correctAnswer

        fun bind(QuestionObject : Question) {

            answer.setText("CORRECT ANSWER: " + QuestionObject.answer)
            question.setText(QuestionObject.question)

        }
    }
}