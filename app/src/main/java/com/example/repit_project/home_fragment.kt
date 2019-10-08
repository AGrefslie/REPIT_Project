package com.example.repit_project


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.repit_project.Data.TestData
import com.example.repit_project.RecyclerViewAdapter.QuizAdapter
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_home.*


class home_fragment : Fragment() {

    private lateinit var quizAdapter: QuizAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)

        /*
        initRecyclerView()
        addDataSet()
         */
    }

    /*
    //Legger til data fra testklassen
    private fun addDataSet() {
        val data = TestData.createDataSet()
        quizAdapter.submitList(data)
    }

    //Initsialiserer recyckerViewet
    private fun initRecyclerView() {
        recycler_view.apply {
            layoutManager = LinearLayoutManager(activity)
            quizAdapter = QuizAdapter()
            adapter = quizAdapter
        }
    }
    */

    

}
