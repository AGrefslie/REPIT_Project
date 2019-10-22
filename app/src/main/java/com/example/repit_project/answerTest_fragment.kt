package com.example.repit_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_answer_test.*
import androidx.navigation.fragment.findNavController


class answerTest_fragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_answer_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val position = answerTest_fragmentArgs.fromBundle(arguments!!).quizPosition
        val quizList = home_fragment.QuizList
        var quizStartValue = 0

        questionText.setText(quizList[position].questions[quizStartValue].question)

        viewAnswerBtn.setOnClickListener {
            viewAnswer(quizList[position].questions[quizStartValue].answer)
        }


        submitAnswer.setOnClickListener {
            if (quizStartValue > quizList[position].questions.size) {

                val goToFeedbackAction = answerTest_fragmentDirections.actionAnswerTestFragmentToQuizFeedback()
                findNavController().navigate(goToFeedbackAction)

            } else {

                if (quizList[position].questions[quizStartValue].answer.toUpperCase() == userAnswer.text.toString().toUpperCase()) {
                    //Sets Answer to black, and feedback to blank
                    userAnswer.setText("")
                    answerFeedbackText.setText("")

                    //Goes to next question if answer is correct
                    quizStartValue = quizStartValue + 1
                    questionText.setText(quizList[position].questions[quizStartValue].question)

                    //Feedback on correct answer
                    Toast.makeText(context, "RIKITG!!!", Toast.LENGTH_SHORT).show()

                } else {
                    //Gives feedback on wrong answer
                    answerFeedbackText.setText("FEIL! Prøv igjen")
                }
            }

        }


    }

    fun viewAnswer(answer : String) {
        answerFeedbackText.setText(answer)
        //Toast.makeText(context, answer, Toast.LENGTH_LONG).show()
    }

}