package com.example.repit_project


import android.app.*
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.hardware.SensorManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz
import com.example.repit_project.Models.UserDetail
import com.example.repit_project.RecyclerViewAdapter.QuestionListAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_create_test.*
import kotlinx.android.synthetic.main.fragment_create_test.addQuestionBtn
import kotlinx.android.synthetic.main.fragment_create_test.viewQuestionsBtn
import kotlinx.android.synthetic.main.fragment_edit_test.*
import kotlinx.android.synthetic.main.fragment_user.*
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class edit_test_fragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var collectionQuizes : CollectionReference
    private lateinit var mStorageRef: FirebaseStorage

    private lateinit var quizTitle : String
    private lateinit var quizDescription : String
    private lateinit var quizImageUri : Uri

    private var quizPrivacey = false

    private lateinit var inputQuestion : String
    private lateinit var inputAnswer : String
    private lateinit var questionList : ArrayList<Question>

    private lateinit var editQuizObject : Quiz

    private lateinit var documentId : String

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable

    private val SELECT_PICTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        collectionQuizes = db.collection("Quizes")
        mStorageRef = FirebaseStorage.getInstance()

        deleteIcon = ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_delete_small)!!

        documentId = edit_test_fragmentArgs.fromBundle(arguments!!).documentId
        Log.d("did",documentId)

        getDataFromFireStore()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_test, container, false)
        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addQuestionBtn.setOnClickListener {
            openQuestionDialog()
        }

        viewQuestionsBtn.setOnClickListener {
            openQuestionList()
        }

        quizImageUri = (R.drawable.ic_launcher_foreground.toString().toUri())
        updateImageBtn.setOnClickListener {
            addQuizImage()
        }

        fabEditTest.setOnClickListener {
            addQuizToFirestore()
        }

    }

    private fun getDataFromFireStore() {
        val picasso = Picasso.get()
        val docRef = collectionQuizes.document(documentId)
        docRef.get().addOnCompleteListener { task ->
            val document = task.getResult()
            if ( document != null){
                if (task.isSuccessful) {
                    val thisQuiz = document.toObject(Quiz::class.java)
                    editTestTitle.setText(thisQuiz?.title)
                    editTestDescription.setText(thisQuiz?.description)
                    picasso.load(thisQuiz?.image).into(updateCoverImage)
                    questionList = thisQuiz?.questions as ArrayList<Question>
                }
                else {
                    Log.d(home_fragment.LOGTAG, "Error getting documents: " + task.exception)
                }
            }
            else {
                Log.d("ASDF", "not null")
            }

        }
    }

    private fun openQuestionDialog() {
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

    private fun openQuestionList() {

        val DialogView = LayoutInflater.from(context).inflate(R.layout.dialog_view_questions, null)


        val Builder = AlertDialog.Builder(context)
            .setView(DialogView)
            .setTitle("Here is list of all your questions")
            .setMessage("Swipe left to delete a question")

        val mYrecylerView = DialogView.findViewById<RecyclerView>(R.id.recycler_view_questions)

        mYrecylerView.adapter = QuestionListAdapter(questionList)
        mYrecylerView.layoutManager = LinearLayoutManager(activity)

        Builder.setPositiveButton("SAVE"){_, _ ->
            Toast.makeText(context, "Question list saved", Toast.LENGTH_SHORT).show()
        }
        Builder.setNegativeButton("CANCEL") {_, _ ->
            Toast.makeText(context,"Adding Question Canceled", Toast.LENGTH_SHORT).show()
        }

        Builder.show()

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, position: Int) {
                (mYrecylerView.adapter as QuestionListAdapter).removeItem(viewHolder)
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

                val itemView = viewHolder.itemView
                val iconMargin = (itemView.height - deleteIcon.intrinsicHeight) / 2

                if (dX < 0) {
                    swipeBackground.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    deleteIcon.setBounds(itemView.right - iconMargin - deleteIcon.intrinsicWidth, itemView.top + iconMargin,
                        itemView.right - iconMargin, itemView.bottom - iconMargin)
                }

                swipeBackground.draw(c)

                c.save()

                if (dX < 0 ) {
                    c.clipRect(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                }

                deleteIcon.draw(c)

                c.restore()
            }


        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(mYrecylerView)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK) {
            try {
                val uri = data!!.data
                quizImageUri = uri!!
                updateCoverImage.setImageURI(uri)
                updateCoverImage.isVisible = true
                editTestImageText.isVisible = false
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
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        quizTitle = editTestTitle.text.toString()
        quizDescription = editTestDescription.text.toString()

        editPublicSwitch.setOnCheckedChangeListener() { _, isChecked ->
            if (isChecked) {
                quizPrivacey = true
            }
        }

        val docRef = collectionQuizes.document(documentId)
        docRef.get().addOnCompleteListener { task ->
            val document = task.getResult()
            val thisQuiz = document?.toObject(Quiz::class.java)

            val mQuiz = Quiz("0", quizTitle, quizDescription, thisQuiz!!.image, quizPrivacey, questionList, firebaseUser!!.uid)

            var file = quizImageUri
            val mImageRef = mStorageRef.reference.child("images/${file.lastPathSegment}")
            val uploadTask = mImageRef.putFile(file)

            uploadTask.addOnFailureListener {
                Log.d("Upload: ", "Failed: $it")
            }.addOnSuccessListener {
                mQuiz.image = it.toString()
            }

            collectionQuizes.document(documentId).set(mQuiz, SetOptions.merge())
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot written with ID: $documentId")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }



        val action = edit_test_fragmentDirections.actionEditTestFragmentToDestinationHome()
        findNavController().navigate(action)

    }

    override fun onResume() {
        super.onResume()

        //make sure that the keyboard dont push the view when activated
        getActivity()?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

}
