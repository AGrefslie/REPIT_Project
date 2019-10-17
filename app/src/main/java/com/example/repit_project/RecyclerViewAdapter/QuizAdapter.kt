package com.example.repit_project.RecyclerViewAdapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.repit_project.Models.Quiz
import com.example.repit_project.R
import com.example.repit_project.answerTest_fragment
import com.example.repit_project.home_fragment
import com.example.repit_project.home_fragmentDirections
import kotlinx.android.synthetic.main.layout_listitem.view.*

class QuizAdapter(private val list: List<Quiz>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items : List<Quiz> = list


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return QuizViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_listitem, parent, false)
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {
            is QuizViewHolder -> {
                holder.bind(items.get(position))


                val currentQuizItem = items[position]
                holder.bind(currentQuizItem)

                holder.startTestButton.setOnClickListener{ view ->
                    Toast.makeText(view.context, "Test " + position.toString(), Toast.LENGTH_SHORT).show()

                    val action = home_fragmentDirections.actionDestinationHomeToAnswerTestFragment(position)
                    findNavController(view).navigate(action)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun submitList(QuizList : List<Quiz>) {
        items = QuizList
    }

    class QuizViewHolder constructor (itemView : View): RecyclerView.ViewHolder(itemView) {

        val quizImage = itemView.quiz_image
        val quizTitle = itemView.quiz_title
        val quizDescription = itemView.quiz_description
        val startTestButton = itemView.startTestBtn

        fun bind(QuizObject : Quiz) {

            quizTitle.setText(QuizObject.title)
            quizDescription.setText(QuizObject.description)

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(QuizObject.image)
                .into(quizImage)
        }
    }
}