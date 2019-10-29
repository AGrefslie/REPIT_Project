package com.example.repit_project


import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_add_question.*
import kotlinx.android.synthetic.main.dialog_add_question.view.*
import kotlinx.android.synthetic.main.fragment_create_test.*
import kotlinx.android.synthetic.main.fragment_user.*
import java.io.IOException


class create_test_fragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var collectionQuizes : CollectionReference

    private lateinit var quizTitle : String
    private lateinit var quizDesction : String
    private lateinit var quizImageUri : Uri

    private lateinit var oneQuestion : String
    private lateinit var oneAnswer : String
    private lateinit var questionList : ArrayList<Question>

    private val SELECT_PICTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        collectionQuizes = db.collection("Quizes")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_test, container, false)
        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addQuestionBtn.setOnClickListener {
            openQuestionDialog()
        }

        addImageBtn.setOnClickListener {
            addQuizImage()
        }

        fabCreateTest.setOnClickListener {
            addQuizToFirestore()
        }
    }

    fun openQuestionDialog() {
        val DialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_question, null)

        val Builder = AlertDialog.Builder(context)
            .setView(DialogView)
            .setTitle("Add Question")
            .setMessage("Fill in the fields below to add a question")



        Builder.setPositiveButton("Add Question"){dialog, which ->

            oneQuestion = edit_question.text.toString()
            oneAnswer = edit_answer.text.toString()

            questionList.add(Question(oneQuestion, oneAnswer))

            Toast.makeText(context, "Question Added", Toast.LENGTH_SHORT).show()
        }

        Builder.setNegativeButton("Cancel"){dialog,which ->
            Toast.makeText(context,"Adding Question Canceled", Toast.LENGTH_SHORT).show()
        }

        Builder.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                quizImageUri = uri!!
                coverImage.setImageURI(uri)
                coverImage.isVisible = true
                addTestImageText.isVisible = false
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun addQuizImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_PICTURE)
    }

    fun addQuizToFirestore() {
        quizTitle = testTitle.text.toString()
        quizDesction = testDescription.text.toString()

        Log.i("Title: ", quizTitle)
        Log.i("Description: ", quizDesction)


    }

}
