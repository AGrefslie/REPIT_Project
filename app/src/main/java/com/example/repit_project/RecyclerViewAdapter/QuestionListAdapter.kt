package com.example.repit_project.RecyclerViewAdapter

import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz
import com.example.repit_project.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.layout_listitem_wrong_answer.view.*

class QuestionListAdapter(private val list: ArrayList<Question>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items : ArrayList<Question> = list

    private var removedPosition: Int = 0
    private var removedItem: Question = Question()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QuestionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem_questionlist, parent, false)
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

    fun removeItem(viewHolder: RecyclerView.ViewHolder){

        removedPosition = viewHolder.adapterPosition
        removedItem = list[viewHolder.adapterPosition]

        list.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)

        Snackbar.make(viewHolder.itemView, "Question deleted. ", Snackbar.LENGTH_LONG).setAction("UNDO") {
            list.add(removedPosition, removedItem)
            notifyItemInserted(removedPosition)
        }.show()

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