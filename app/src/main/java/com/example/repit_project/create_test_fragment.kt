package com.example.repit_project


import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz
import com.example.repit_project.RecyclerViewAdapter.QuestionListAdapter
import com.example.repit_project.RecyclerViewAdapter.QuizAdapter
import com.example.repit_project.RecyclerViewAdapter.WrongAnswerAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.dialog_date_picker.*
import kotlinx.android.synthetic.main.dialog_view_questions.*
import kotlinx.android.synthetic.main.fragment_create_test.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_listitem.*
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

    private var swipeBackground: ColorDrawable = ColorDrawable(Color.parseColor("#FF0000"))
    private lateinit var deleteIcon: Drawable

    private val SELECT_PICTURE = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        collectionQuizes = db.collection("Quizes")

        deleteIcon = ContextCompat.getDrawable(this.requireContext(), R.drawable.ic_delete_small)!!

        questionList = ArrayList()

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_test, container, false)
        
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        reminderBtn.setOnClickListener {
            openReminderDialog()
        }

        addQuestionBtn.setOnClickListener {
            openQuestionDialog()
        }

        viewQuestionsBtn.setOnClickListener {
            openQuestionList()
        }

        quizImageUri = (R.drawable.ic_launcher_foreground.toString().toUri())
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

    private fun openReminderDialog() {
        val notificationId = 1

        val intent = Intent(context, AlarmReciver::class.java)
        intent.putExtra("notificationId", notificationId)
        intent.putExtra("todo", reminderText.text.toString())

        val mAlarmIntent = PendingIntent.getBroadcast(context, 0, intent,
            PendingIntent.FLAG_CANCEL_CURRENT)

        val alarm : AlarmManager? = context?.getSystemService(AlarmManager::class.java) as AlarmManager

        val DialogView = LayoutInflater.from(context).inflate(R.layout.dialog_date_picker, null)

        val Builder = AlertDialog.Builder(context)
            .setView(DialogView)
            .setTitle("Pick a time")
            .setMessage("Set the time to when you want a reminder")

        Builder.setPositiveButton("SET"){_, _ ->
            Toast.makeText(context, "Reminder set", Toast.LENGTH_SHORT).show()
        }
        Builder.setNegativeButton("CANCEL") {_, _ ->
            Toast.makeText(context,"Reminder Canceld", Toast.LENGTH_SHORT).show()
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
