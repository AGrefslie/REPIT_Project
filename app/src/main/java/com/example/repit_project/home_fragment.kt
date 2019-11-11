package com.example.repit_project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repit_project.Models.Question
import com.example.repit_project.Models.Quiz
import com.google.firebase.firestore.DocumentChange.Type
import com.example.repit_project.RecyclerViewAdapter.QuizAdapter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_home.*



class home_fragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var collectionQuizes : CollectionReference

    private lateinit var fireStoreListenerRegistration: ListenerRegistration

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        db = FirebaseFirestore.getInstance()
        collectionQuizes = db.collection("Quizes")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        //Setup FAB navigation
        FAB_CreateTestButton.setOnClickListener {
            val action = home_fragmentDirections.actionDestinationHomeToDestinationCreateTest()
            findNavController().navigate(action)
        }

        recycler_view.adapter = QuizAdapter(quizList)
        recycler_view.layoutManager = LinearLayoutManager(activity)


        //genereateTestData()

    }

    private fun createFireStoreReadListner() {
        fireStoreListenerRegistration = collectionQuizes.addSnapshotListener { querySnapshot, exception ->
            if (exception != null) {
                Log.w(LOGTAG, "Listen failed", exception)
                return@addSnapshotListener
            }
            for (documentChange in querySnapshot?.documentChanges!!) {
                val documentSnapshot = documentChange.document
                val Quiz = documentSnapshot.toObject(Quiz::class.java)
                Quiz.uid = documentSnapshot.id

                val pos = quizListUid.indexOf(Quiz.uid)

                when (documentChange.type) {
                    Type.ADDED -> {
                        quizList.add(Quiz)
                        quizListUid.add(Quiz.uid)
                        recycler_view.adapter?.notifyItemInserted(quizList.size-1)
                    }
                    Type.REMOVED -> {
                        quizList.removeAt(pos)
                        quizListUid.removeAt(pos)
                        recycler_view.adapter?.notifyItemRemoved(pos)
                    }
                    Type.MODIFIED -> {
                        quizList[pos] = Quiz
                        recycler_view.adapter?.notifyItemChanged(pos)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        createFireStoreReadListner()
    }

    override fun onPause() {
        super.onPause()
        quizList = ArrayList<Quiz>()
        quizListUid = ArrayList<String>()
        fireStoreListenerRegistration.remove()
    }

    companion object {

        var quizList : MutableList<Quiz> = ArrayList()
        var quizListUid : MutableList<String> = ArrayList()

        private val QuestionList1 = listOf(
            Question("Hva er Hei på Spansk?", "Hola"),
            Question("Hva er Hei på Engelsk?", "Hello")
        )

        private val QuestionList2 = listOf(
            Question("Hvem er kaptein for Manchester United?", "Ashley Young"),
            Question("Hvem er norges største tallent i golf i dag?", "Viktor Hovland"),
            Question("Hvem er manager for Manchester United i 2019", "Ole Gunnar Solskjær")
        )

        val QuizListTestData = listOf(
            Quiz("0","Hello Quiz", "Hello in different languages", "https://i.ytimg.com/vi/kJ2dr9jAThY/maxresdefault.jpg", false, QuestionList1),
            Quiz("1","MUFC & GOLF", "No description needed", "https://www.soccerpro.com/wp-content/uploads/2018/02/ManchesterUnited_1280x800.jpg", false, QuestionList2)
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList)
        )

        private val LOGTAG = MainActivity::class.java.simpleName
    }

    fun genereateTestData() {
        for (Quiz in QuizListTestData) {
            collectionQuizes.add(Quiz)
        }
    }

}

