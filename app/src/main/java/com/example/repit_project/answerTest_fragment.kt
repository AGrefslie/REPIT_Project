package com.example.repit_project

import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_answer_test.*
import androidx.navigation.fragment.findNavController
import com.example.repit_project.Models.Question


class answerTest_fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_answer_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val position = answerTest_fragmentArgs.fromBundle(arguments!!).quizPosition
        val quizList = home_fragment.quizList
        var quizStartValue = 0

        questionText.setText(quizList[position].questions[quizStartValue].question)


        viewAnswerBtn.setOnClickListener {
            viewAnswer(quizList[position].questions[quizStartValue].answer)
        }


        submitAnswer.setOnClickListener {

            if (quizList[position].questions[quizStartValue].answer.toUpperCase() == userAnswer.text.toString().toUpperCase()) {

                userAnswer.setText("")
                answerFeedbackText.setText("")

                correctAnswerList.add(quizList[position].questions[quizStartValue].question)

                quizStartValue += 1
                resultFromQuiz += 1

                if(quizStartValue + 1 > quizList[position].questions.size){
                    //Log.w("Correct List: ", correctAnswerList.toString())

                    val action = answerTest_fragmentDirections.actionAnswerTestFragmentToQuizFeedback()
                    findNavController().navigate(action)

                } else {
                    questionText.setText(quizList[position].questions[quizStartValue].question)
                }

            } else {

                userAnswer.setText("")
                answerFeedbackText.setText("")

                wrongAnswerList.add(quizList[position].questions[quizStartValue])
                quizStartValue += 1


                if(quizStartValue + 1 > quizList[position].questions.size){
                    val action = answerTest_fragmentDirections.actionAnswerTestFragmentToQuizFeedback()
                    findNavController().navigate(action)

                    //Log.w("Wrong List: ", wrongAnswerList.toString())
                } else {
                    questionText.setText(quizList[position].questions[quizStartValue].question)
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()

        correctAnswerList = ArrayList()
        wrongAnswerList = ArrayList()
        resultFromQuiz = 0
    }

    companion object {
        var correctAnswerList = ArrayList<String>()
        var wrongAnswerList = ArrayList<Question>()

        var resultFromQuiz = 0
    }

    fun viewAnswer(answer : String) {
        answerFeedbackText.setText(answer)
    }

}