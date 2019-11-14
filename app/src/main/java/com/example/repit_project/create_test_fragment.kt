package com.example.repit_project


import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_create_test.*
import java.io.IOException


class create_test_fragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var collectionQuizes : CollectionReference
    private lateinit var user : FirebaseAuth

    private lateinit var quizTitle : String
    private lateinit var quizDescription : String
    private lateinit var quizImageUri : Uri
    var quizPrivacey = false

    private lateinit var inputQuestion : String
    private lateinit var inputAnswer : String
    private lateinit var questionList : ArrayList<Question>

    private val SELECT_PICTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        collectionQuizes = db.collection("Quizes")

        questionList = ArrayList()

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

        publicSwitch.setOnCheckedChangeListener() { _, isChecked ->
            if (isChecked) {
                quizPrivacey = true
            }
        }
    }

    fun openQuestionDialog() {
        val DialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_question, null)

        val Builder = AlertDialog.Builder(context)
            .setView(DialogView)
            .setTitle("Add Question")
            .setMessage("Fill in the fields below to add a question")

        val editQuestion = DialogView.findViewById<EditText>(R.id.edit_question)
        val editAnswer = DialogView.findViewById<EditText>(R.id.edit_answer)


        Builder.setPositiveButton("Add Question"){_, _ ->
            inputQuestion = editQuestion.text.toString()
            inputAnswer = editAnswer.text.toString()

            questionList.add(Question(inputQuestion, inputAnswer))

            Toast.makeText(context, "Question Added", Toast.LENGTH_SHORT).show()
        }

        Builder.setNegativeButton("Cancel"){_,_ ->
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
        quizDescription = testDescription.text.toString()

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        val mQuiz = Quiz("0", quizTitle, quizDescription, quizImageUri.toString(), quizPrivacey, questionList, firebaseUser!!.uid)


        collectionQuizes.add(mQuiz)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }


        val action = create_test_fragmentDirections.actionDestinationCreateTestToDestinationHome()
        findNavController().navigate(action)
    }

}
