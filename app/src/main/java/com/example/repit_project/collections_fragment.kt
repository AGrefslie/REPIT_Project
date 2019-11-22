package com.example.repit_project


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repit_project.Models.Quiz
import com.example.repit_project.RecyclerViewAdapter.QuizAdapter
import com.example.repit_project.RecyclerViewAdapter.QuizAdapterCollections
import com.example.repit_project.home_fragment.Companion.LOGTAG
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.android.synthetic.main.fragment_collections.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.recycler_view

class collections_fragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var collectionQuizes : CollectionReference
    private lateinit var fireStoreListenerRegistration: ListenerRegistration

    private var quizList : MutableList<Quiz> = ArrayList()
    private var quizListUid : MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        db = FirebaseFirestore.getInstance()
        collectionQuizes = db.collection("Quizes")

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_collections, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler_view.adapter = QuizAdapterCollections(quizList, 2)
        recycler_view.layoutManager = GridLayoutManager(context, 2)
    }

    private fun createFireStoreReadListner() {

        fireStoreListenerRegistration = collectionQuizes
            .whereEqualTo("public", true)
            .addSnapshotListener { querySnapshot, exception ->
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
                        DocumentChange.Type.ADDED -> {
                            quizList.add(Quiz)
                            quizListUid.add(Quiz.uid)
                            recycler_view.adapter?.notifyItemInserted(quizList.size-1)
                        }
                        DocumentChange.Type.REMOVED -> {
                            quizList.removeAt(pos)
                            quizListUid.removeAt(pos)
                            recycler_view.adapter?.notifyItemRemoved(pos)
                        }
                        DocumentChange.Type.MODIFIED -> {
                            quizList[pos] = Quiz
                            recycler_view.adapter?.notifyItemChanged(pos)
                        }
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()

        searchField.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                search(query)
                return false
            }

        })
    }

    fun search(str : String) {
        val searchedQuizList = ArrayList<Quiz>()
        for (Quiz in quizList) {

            if (Quiz.title.toLowerCase().contains(str.toLowerCase())){
                searchedQuizList.add(Quiz)
            }

        }

        recycler_view.adapter = QuizAdapterCollections(searchedQuizList, 2)
        recycler_view.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onResume() {
        super.onResume()
        createFireStoreReadListner()

        //make sure that the keyboard dont push the view when activated
        getActivity()?.getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
    }

    override fun onPause() {
        super.onPause()
        quizList = ArrayList<Quiz>()
        quizListUid = ArrayList<String>()
        fireStoreListenerRegistration.remove()
    }
}
