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

    private var quizList : MutableList<Quiz> = ArrayList()
    private var quizListUid : MutableList<String> = ArrayList()

    private lateinit var quizAdapter : QuizAdapter

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

        //genereateTestData()

        createFireStoreReadListner()

        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            quizAdapter = QuizAdapter(quizList)
        }
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
                val QuizUid = documentSnapshot.id

                val pos = quizListUid.indexOf(QuizUid)

                when (documentChange.type) {
                    Type.ADDED -> {
                        quizList.add(Quiz)
                        quizListUid.add(QuizUid)
                        quizAdapter.notifyItemInserted(quizList.size-1)
                    }
                    Type.REMOVED -> {
                        quizList.removeAt(pos)
                        quizListUid.removeAt(pos)
                        quizAdapter.notifyItemRemoved(pos)
                    }
                    Type.MODIFIED -> {
                        quizList[pos] = Quiz
                        quizAdapter.notifyItemChanged(pos)
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
        fireStoreListenerRegistration.remove()
    }

    companion object {
        private val QuestionList1 = listOf(
            Question("Hva er Hei på Spansk?", "Hola"),
            Question("Hva er Hei på Engelsk?", "Hello")
        )

        private val QuestionList2 = listOf(
            Question("Hvem er kaptein for Manchester United?", "Ashley Young"),
            Question("Hvem er norges største tallent i golf i dag?", "Viktor Hovland"),
            Question("Hvem er manager for Manchester United i 2019", "Ole Gunnar Solskjær")
        )

        val QuizList = listOf(
            Quiz("Hello Quiz", "Hello in different languages", "https://i.ytimg.com/vi/kJ2dr9jAThY/maxresdefault.jpg", false, QuestionList1),
            Quiz("MUFC & GOLF", "No description needed", "https://www.soccerpro.com/wp-content/uploads/2018/02/ManchesterUnited_1280x800.jpg", false, QuestionList2)
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList),
            //Quiz("Test Quiz", "This is just a test", "https://images.unsplash.com/photo-1524646432175-d58115a8a854?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1050&q=80", false, QuestionList)
        )

        private val LOGTAG = MainActivity::class.java.simpleName
    }

    fun genereateTestData() {
        /*var QuestionList1 = ArrayList<Question>()
        var QuestionList2 = ArrayList<Question>()

        QuestionList1.add(Question("Hvem er kaptein for Manchester United?", "Ashley Young"))
        QuestionList1.add(Question("Hvem er norges største tallent i golf i dag?", "Viktor Hovland"))
        QuestionList1.add(Question("Hvem er manager for Manchester United i 2019", "Ole Gunnar Solskjær"))

        QuestionList2.add(Question("Hva er Hei på Spansk?", "Hola"))
        QuestionList2.add(Question("Hva er Hei på Engelsk?", "Hello"))

        quizList.add(Quiz("Hello Quiz", "Hello in different languages", "https://i.ytimg.com/vi/kJ2dr9jAThY/maxresdefault.jpg", false, QuestionList1))
        quizList.add(Quiz("MUFC & GOLF", "No description needed", "https://www.soccerpro.com/wp-content/uploads/2018/02/ManchesterUnited_1280x800.jpg", false, QuestionList2))
        */

        for (Quiz in QuizList) {
            collectionQuizes.add(Quiz)
        }
    }

}

