package com.example.repit_project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_answer_test.*
import androidx.navigation.fragment.findNavController
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz


class answerTest_fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_answer_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val quiz = answerTest_fragmentArgs.fromBundle(arguments!!).quizList

        var quizStartValue = 0

        questionText.setText(quiz.questions[quizStartValue].question)


        viewAnswerBtn.setOnClickListener {
            viewAnswer(quiz.questions[quizStartValue].answer)
        }


        submitAnswer.setOnClickListener {

            if (quiz.questions[quizStartValue].answer.toUpperCase() == userAnswer.text.toString().toUpperCase()) {

                userAnswer.setText("")
                answerFeedbackText.setText("")

                correctAnswerList.add(quiz.questions[quizStartValue].question)

                quizStartValue += 1
                resultFromQuiz += 1

                if(quizStartValue + 1 > quiz.questions.size){
                    //Log.w("Correct List: ", correctAnswerList.toString())

                    val action = answerTest_fragmentDirections.actionAnswerTestFragmentToQuizFeedback()
                    findNavController().navigate(action)

                } else {
                    questionText.setText(quiz.questions[quizStartValue].question)
                }

            } else {

                userAnswer.setText("")
                answerFeedbackText.setText("")

                wrongAnswerList.add(quiz.questions[quizStartValue])
                quizStartValue += 1


                if(quizStartValue + 1 > quiz.questions.size){
                    val action = answerTest_fragmentDirections.actionAnswerTestFragmentToQuizFeedback()
                    findNavController().navigate(action)

                    //Log.w("Wrong List: ", wrongAnswerList.toString())
                } else {
                    questionText.setText(quiz.questions[quizStartValue].question)
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