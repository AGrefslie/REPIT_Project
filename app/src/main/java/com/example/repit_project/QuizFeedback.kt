package com.example.repit_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repit_project.RecyclerViewAdapter.CorrectAnswerAdapter
import com.example.repit_project.RecyclerViewAdapter.WrongAnswerAdapter
import kotlinx.android.synthetic.main.fragment_feedback.*

class QuizFeedback : Fragment() {

    private var mCorrectAnswers = answerTest_fragment.correctAnswerList
    private var mWrongAnswers = answerTest_fragment.wrongAnswerList

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.w("list: ", mWrongAnswers.toString())

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feedback, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        correctAnswer_recycler_view.adapter = CorrectAnswerAdapter(mCorrectAnswers)
        correctAnswer_recycler_view.layoutManager = LinearLayoutManager(activity)

        wrongAnswer_recycler_view.adapter = WrongAnswerAdapter(mWrongAnswers)
        wrongAnswer_recycler_view.layoutManager = LinearLayoutManager(activity)

        fabEndTest.setOnClickListener {
            val action = QuizFeedbackDirections.actionQuizFeedbackToDestinationHome()
            findNavController().navigate(action)
        }

    }

}